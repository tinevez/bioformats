package ome.scifio.in.apng;

import ome.scifio.Field;

/**
 * APNG helper class, providing a struct representing
 * a fcTL chunk.
 *
 */
public class APNGfcTLChunk extends APNGChunk {

  // -- Fields --
  
  /** Sequence number of the animation chunk, starting from 0 */
  @Field(label = "sequence_number")
  public int sequenceNumber;
  
  /** Width of the following frame */
  @Field(label = "width")
  public int width;
  
  /** Height of the following frame */
  @Field(label = "height")
  public int height;
  
  /** X position at which to render the following frame */
  @Field(label = "x_offset")
  public int xOffset;
  
  /** Y position at which to render the following frame */
  @Field(label = "y_offset")
  public int yOffset;
  
  /** Frame delay fraction numerator */
  @Field(label = "delay_num")
  public short delayNum;
  
  /** Frame delay fraction denominator */
  @Field(label = "delay_den")
  public short delayDen;
  
  /** Type of frame area disposal to be done after rendering this frame */
  @Field(label = "dispose_op")
  public byte disposeOp;
  
  /** Type of frame area rendering for this frame */
  @Field(label = "blend_op")
  public byte blendOp;
  
  // -- Methods --
  @Override
  public int[] getFrameCoordinates() {
    return new int[]{xOffset, yOffset, width, height};
  }
}
