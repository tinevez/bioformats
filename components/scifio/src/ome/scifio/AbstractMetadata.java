package ome.scifio;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class AbstractMetadata implements Metadata {

  /* @see Metadata#resetMeta(Class<?>) */
  public void resetMeta(final Class<?> type) {
    if(type == null || type.equals(AbstractMetadata.class)) return;
    
    for(final Field f : type.getDeclaredFields()) {
      f.setAccessible(true);
      
      if(Modifier.isFinal(f.getModifiers())) continue;
      Class<?> fieldType = f.getType();
      try {
        if (fieldType == boolean.class) f.set(this, false);
        else if (fieldType == char.class) f.set(this, 0);
        else if (fieldType == double.class) f.set(this, 0.0);
        else if (fieldType == float.class) f.set(this, 0f);
        else if (fieldType == int.class) f.set(this, 0);
        else if (fieldType == long.class) f.set(this, 0l);
        else if (fieldType == short.class) f.set(this, 0);
        else f.set(this, null);
      }
      catch (IllegalArgumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      catch (IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      // check superclasses and interfaces
      resetMeta(type.getSuperclass());
      for (final Class<?> c : type.getInterfaces()) {
        resetMeta(c);
      }
    }
  }
}
