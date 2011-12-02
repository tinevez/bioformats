package ome.scifio.out.apng;

import java.awt.image.IndexColorModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.DeflaterOutputStream;

import ome.scifio.AbstractWriter;
import ome.scifio.FormatException;
import ome.scifio.common.DataTools;
import ome.scifio.in.apng.APNGMetadata;
import ome.scifio.in.apng.APNGfcTLChunk;
import ome.scifio.util.FormatTools;

public class APNGWriter extends AbstractWriter<APNGMetadata> {

  // -- Constants --

  private static final byte[] PNG_SIG = new byte[] {
    (byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a
  };

  // -- Fields --

  private int numFrames = 0;
  private long numFramesPointer = 0;
  private int nextSequenceNumber;
  private boolean littleEndian;
  
  // -- Constructor --
  
  public APNGWriter() {
    super("Animated PNG", "png");
  }

  // -- Writer API Methods --
  
  /**
   * @see ome.scifio.Writer#saveBytes(int, byte[], int, int, int, int)
   */
  public void saveBytes(int iNo, int no, byte[] buf, int x, int y, int w, int h)
    throws FormatException, IOException
  {
    checkParams(iNo, no, buf, x, y, w, h);
    if (!isFullPlane(iNo, x, y, w, h)) {
      throw new FormatException(
        "APNGWriter does not yet support saving image tiles.");
    }

    //int width = metadata.getSizeX(iNo);
    //int height = metadata.getSizeY(iNo);

    if (!initialized[iNo][no]) {
      writeFCTL();
      if (numFrames == 0) writePLTE();
      initialized[iNo][no] = true;
    }

    writePixels(iNo, numFrames == 0 ? "IDAT" : "fdAT", buf, x, y, w, h);
    numFrames++;
  }

  // -- MetadataHandler methods --
  
  @Override
  public Class<APNGMetadata> getMetadataType() {
    // TODO Auto-generated method stub
    return null;
  }

  // -- APNGWriter Methods --
  
  /* @see ome.scifio.Writer#close() */
  public void close() throws IOException {
    if (out != null) {
      writeFooter();
    }
    super.close();
    numFrames = 0;
    numFramesPointer = 0;
    nextSequenceNumber = 0;
    littleEndian = false;
  }
  
  // TODO where to call this?  Should it be in the API?
  @Override
  public void initialize(int iNo) throws FormatException, IOException {
    super.initialize(iNo);
    if (out.length() == 0) {
      int width = metadata.getSizeX(iNo);
      int height = metadata.getSizeY(iNo);
      int bytesPerPixel =
        FormatTools.getBytesPerPixel(metadata.getPixelType(iNo));
      int nChannels = metadata.getEffectiveSizeC(iNo);
      boolean indexed =
        getColorModel() != null && (getColorModel() instanceof IndexColorModel);
      littleEndian = metadata.isLittleEndian(iNo);

      // write 8-byte PNG signature
      out.write(PNG_SIG);

      // write IHDR chunk

      out.writeInt(13);
      byte[] b = new byte[17];
      b[0] = 'I';
      b[1] = 'H';
      b[2] = 'D';
      b[3] = 'R';

      DataTools.unpackBytes(width, b, 4, 4, false);
      DataTools.unpackBytes(height, b, 8, 4, false);

      b[12] = (byte) (bytesPerPixel * 8);
      if (indexed) b[13] = (byte) 3;
      else if (nChannels == 1) b[13] = (byte) 0;
      else if (nChannels == 2) b[13] = (byte) 4;
      else if (nChannels == 3) b[13] = (byte) 2;
      else if (nChannels == 4) b[13] = (byte) 6;
      b[14] = (byte) 0;
      b[15] = (byte) 0;
      b[16] = (byte) 0;

      out.write(b);
      out.writeInt(crc(b));

      // write acTL chunk

      out.writeInt(8);
      out.writeBytes("acTL");
      numFramesPointer = out.getFilePointer();
      out.writeInt(0);
      out.writeInt(0);
      out.writeInt(0); // save a place for the CRC
    }
  }
  
  private int crc(byte[] buf) {
    return crc(buf, 0, buf.length);
  }

  private int crc(byte[] buf, int off, int len) {
    CRC32 crc = new CRC32();
    crc.update(buf, off, len);
    return (int) crc.getValue();
  }

  private void writeFCTL() throws IOException {
    APNGfcTLChunk fctlChunk = (APNGfcTLChunk) metadata.getBlocks().get(nextSequenceNumber);
    out.writeInt(26);
    byte[] b = new byte[30];
    b[0] = 'f';
    b[1] = 'c';
    b[2] = 'T';
    b[3] = 'L';

    DataTools.unpackBytes(nextSequenceNumber++, b, 4, 4, false);
    DataTools.unpackBytes(fctlChunk.width, b, 8, 4, false);
    DataTools.unpackBytes(fctlChunk.height, b, 12, 4, false);
    DataTools.unpackBytes(fctlChunk.xOffset, b, 16, 4, false);
    DataTools.unpackBytes(fctlChunk.yOffset, b, 20, 4, false);
    DataTools.unpackBytes(fctlChunk.delayNum, b, 24, 2, false);
    DataTools.unpackBytes(fctlChunk.delayDen, b, 26, 2, false);
    b[28] = (byte) fctlChunk.disposeOp;
    b[29] = (byte) fctlChunk.blendOp;

    out.write(b);
    out.writeInt(crc(b));
  }

  private void writePLTE() throws IOException {
    if (!(getColorModel() instanceof IndexColorModel)) return;

    IndexColorModel model = (IndexColorModel) getColorModel();
    byte[][] lut = new byte[3][256];
    model.getReds(lut[0]);
    model.getGreens(lut[1]);
    model.getBlues(lut[2]);

    out.writeInt(768);
    byte[] b = new byte[772];
    b[0] = 'P';
    b[1] = 'L';
    b[2] = 'T';
    b[3] = 'E';

    for (int i=0; i<lut[0].length; i++) {
      for (int j=0; j<lut.length; j++) {
        b[i*lut.length + j + 4] = lut[j][i];
      }
    }

    out.write(b);
    out.writeInt(crc(b));
  }

  private void writePixels(int iNo, String chunk, byte[] stream, int x, int y,
    int width, int height) throws FormatException, IOException
  {
    int sizeC = metadata.getEffectiveSizeC(iNo);
    String type = FormatTools.getPixelTypeString(metadata.getPixelType(iNo));
    int pixelType = FormatTools.pixelTypeFromString(type);
    boolean signed = FormatTools.isSigned(pixelType);

    if (!isFullPlane(iNo, x, y, width, height)) {
      throw new FormatException("APNGWriter does not support writing tiles.");
    }

    ByteArrayOutputStream s = new ByteArrayOutputStream();
    s.write(chunk.getBytes());
    if (chunk.equals("fdAT")) {
      s.write(DataTools.intToBytes(nextSequenceNumber++, false));
    }
    DeflaterOutputStream deflater = new DeflaterOutputStream(s);
    int planeSize = stream.length / sizeC;
    int rowLen = stream.length / height;
    int bytesPerPixel = stream.length / (width * height * sizeC);
    byte[] rowBuf = new byte[rowLen];
    for (int i=0; i<height; i++) {
      deflater.write(0);
      if (interleaved) {
        if (littleEndian) {
          for (int col=0; col<width*sizeC; col++) {
            int offset = (i * sizeC * width + col) * bytesPerPixel;
            int pixel = DataTools.bytesToInt(stream, offset,
              bytesPerPixel, littleEndian);
            DataTools.unpackBytes(pixel, rowBuf, col * bytesPerPixel,
              bytesPerPixel, false);
          }
        }
        else System.arraycopy(stream, i * rowLen, rowBuf, 0, rowLen);
      }
      else {
        int max = (int) Math.pow(2, bytesPerPixel * 8 - 1);
        for (int col=0; col<width; col++) {
          for (int c=0; c<sizeC; c++) {
            int offset = c * planeSize + (i * width + col) * bytesPerPixel;
            int pixel = DataTools.bytesToInt(stream, offset, bytesPerPixel,
              littleEndian);
            if (signed) {
              if (pixel < max) pixel += max;
              else pixel -= max;
            }
            int output = (col * sizeC + c) * bytesPerPixel;
            DataTools.unpackBytes(pixel, rowBuf, output, bytesPerPixel, false);
          }
        }
      }
      deflater.write(rowBuf);
    }
    deflater.finish();
    byte[] b = s.toByteArray();

    // write chunk length
    out.writeInt(b.length - 4);
    out.write(b);

    // write checksum
    out.writeInt(crc(b));
  }

  private void writeFooter() throws IOException {
    // write IEND chunk
    out.writeInt(0);
    out.writeBytes("IEND");
    out.writeInt(crc("IEND".getBytes()));

    // update frame count
    out.seek(numFramesPointer);
    out.writeInt(numFrames);
    out.skipBytes(4);
    byte[] b = new byte[12];
    b[0] = 'a';
    b[1] = 'c';
    b[2] = 'T';
    b[3] = 'L';
    DataTools.unpackBytes(numFrames, b, 4, 4, false);
    DataTools.unpackBytes(0, b, 8, 4, false);
    out.writeInt(crc(b));
  }
}
