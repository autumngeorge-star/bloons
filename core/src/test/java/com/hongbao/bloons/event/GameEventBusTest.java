package com.hongbao.bloons.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEventBusTest {

	public static void main(String[] args) {
		System.out.println("Running GameEventBus tests...");
		testSubscribeAndPublish();
		testUnsubscribe();
		testLevelStartedEventPayload();
		testGameStateChangedEventPayload();
		testMultipleSubscribers();
		System.out.println("All GameEventBus tests passed successfully!");
	}

	public static void testSubscribeAndPublish() {
		GameEventBus bus = new GameEventBus();
		List<LevelStartedEvent> received = new ArrayList<>();

		bus.subscribe(LevelStartedEvent.class, received::add);
		bus.publish(new LevelStartedEvent(5));

		assert received.size() == 1 : "Expected 1 event received";
		assert received.get(0).getLevel() == 5 : "Expected level 5";
		assert received.get(0).getLevelIndex() == 5 : "Expected levelIndex 5";
	}

	public static void testUnsubscribe() {
		GameEventBus bus = new GameEventBus();
		List<LevelStartedEvent> received = new ArrayList<>();
		GameEventListener<LevelStartedEvent> listener = received::add;

		bus.subscribe(LevelStartedEvent.class, listener);
		bus.publish(new LevelStartedEvent(1));
		assert received.size() == 1 : "Expected 1 event received before unsubscribe";

		bus.unsubscribe(LevelStartedEvent.class, listener);
		bus.publish(new LevelStartedEvent(2));
		assert received.size() == 1 : "Expected no additional events received after unsubscribe";
	}

	public static void testLevelStartedEventPayload() {
		LevelStartedEvent event = new LevelStartedEvent(40);
		assert event.getLevel() == 40 : "getLevel should return 40";
		assert event.getLevelIndex() == 40 : "getLevelIndex should return 40";
	}

	public static void testGameStateChangedEventPayload() {
		Map<String, Object> meta = new HashMap<>();
		meta.put("difficulty", "hard");

		GameStateChangedEvent event = new GameStateChangedEvent(
				GameStateChangedEvent.GameState.TITLE,
				GameStateChangedEvent.GameState.PLAYING,
				meta
		);

		assert event.getPreviousState() == GameStateChangedEvent.GameState.TITLE : "Previous state mismatch";
		assert event.getNewState() == GameStateChangedEvent.GameState.PLAYING : "New state mismatch";
		assert event.getState() == GameStateChangedEvent.GameState.PLAYING : "getState mismatch";
		assert "hard".equals(event.getMetadata().get("difficulty")) : "Metadata mismatch";
	}

	public static void testMultipleSubscribers() {
		GameEventBus bus = new GameEventBus();
		List<String> log = new ArrayList<>();

		bus.subscribe(LevelStartedEvent.class, e -> log.add("sub1:" + e.getLevel()));
		bus.subscribe(LevelStartedEvent.class, e -> log.add("sub2:" + e.getLevel()));

		bus.publish(new LevelStartedEvent(10));

		assert log.size() == 2 : "Expected 2 log entries";
		assert "sub1:10".equals(log.get(0)) : "First subscriber failure";
		assert "sub2:10".equals(log.get(1)) : "Second subscriber failure";
	}
}
