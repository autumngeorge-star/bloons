package com.hongbao.bloons.targeting;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.PathProgress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TargetingStrategyTest {

    @Mock
    private BloonActor actor1;

    @Mock
    private BloonActor actor2;

    @Mock
    private BloonActor actor3;

    @Mock
    private Bloon bloon1;

    @Mock
    private Bloon bloon2;

    @Mock
    private Bloon bloon3;

    @Test
    @DisplayName("Should return null when bloon list is null or empty")
    void testEmptyCollection() {
        assertNull(TargetingStrategy.selectTarget(null));
        assertNull(TargetingStrategy.selectTarget(Collections.emptyList()));
    }

    @Test
    @DisplayName("Should prioritize bloon with highest normalized path progress using Mockito mocks")
    void testPrioritizeHighestPathProgress() {
        PathProgress progress1 = new PathProgress(1, 0.2f, 70.0f); // normalized = 1.2
        PathProgress progress2 = new PathProgress(2, 0.5f, 125.0f); // normalized = 2.5
        PathProgress progress3 = new PathProgress(0, 0.9f, 45.0f); // normalized = 0.9

        when(actor1.getBloon()).thenReturn(bloon1);
        when(actor2.getBloon()).thenReturn(bloon2);
        when(actor3.getBloon()).thenReturn(bloon3);

        when(bloon1.getPathProgress()).thenReturn(progress1);
        when(bloon2.getPathProgress()).thenReturn(progress2);
        when(bloon3.getPathProgress()).thenReturn(progress3);

        List<BloonActor> candidates = Arrays.asList(actor1, actor2, actor3);

        BloonActor selected = TargetingStrategy.selectTarget(candidates);

        assertEquals(actor2, selected, "Targeting strategy should select actor2 which has the highest path progress (2.5)");
    }
}
