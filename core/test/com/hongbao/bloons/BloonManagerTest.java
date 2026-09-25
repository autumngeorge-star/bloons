package com.hongbao.bloons;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.comparators.SortByZIndex;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class BloonManagerTest {

    @Test
    public void testBloonsToPopBufferFieldExistsAndIsPrivateList() throws Exception {
        Field field = BloonManager.class.getDeclaredField("bloonsToPopBuffer");
        Assert.assertNotNull(field);
        Assert.assertTrue(Modifier.isPrivate(field.getModifiers()));
        Assert.assertEquals(List.class, field.getType());
    }

    @Test
    public void testSortByZIndexSingletonInstance() throws Exception {
        Field field = SortByZIndex.class.getDeclaredField("INSTANCE");
        Assert.assertNotNull(field);
        Assert.assertTrue(Modifier.isPublic(field.getModifiers()));
        Assert.assertTrue(Modifier.isStatic(field.getModifiers()));
        Assert.assertTrue(Modifier.isFinal(field.getModifiers()));
        Assert.assertEquals(SortByZIndex.class, field.getType());
        Assert.assertNotNull(SortByZIndex.INSTANCE);
    }
}
