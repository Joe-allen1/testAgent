package com.qyl.controller;//package org.tianxun.screen.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import javax.websocket.OnClose;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.server.ServerEndpoint;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import javax.websocket.Session;
//
//@Slf4j
//@ServerEndpoint("/play/ws")
//public class AnimationPlayController {
//    private static final Map<String, Session> SESSIONS = new ConcurrentHashMap<>();
//
//    @OnOpen
//    public void onOpen(Session session) {
//        System.out.println("✅ WebSocket onOpen 正在注册");
//        SESSIONS.put(session.getId(), session);
//        log.info("[OPEN] Session {} connected", session.getId());
//    }
//
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        log.info("[MESSAGE] Session {}: {}", session.getId(), message);
//
//        try {
//            switch (message) {
//                case "start" -> handleTimelinePlayback(session, false);
//                case "loop" -> handleTimelinePlayback(session, true);
//                case "replay" -> handleTimelinePlayback(session, false);
//                default -> sendText(session, "Unknown command");
//            }
//        } catch (Exception e) {
//            log.error("[ERROR] Exception while handling message", e);
//        }
//    }
//
//    @OnClose
//    public void onClose(Session session) {
//        SESSIONS.remove(session.getId());
//        log.info("[CLOSE] Session {} closed", session.getId());
//    }
//
//    private void handleTimelinePlayback(Session session, boolean loop) throws Exception {
//        do {
//            sendWeatherInfo(session);
//            sendVehicleEvents(session);
//        } while (loop);
//    }
//
//    private void sendWeatherInfo(Session session) throws Exception {
//        WeatherInfo weather = new WeatherInfo("东南风", 100);
//        TimelinePushPayload payload = new TimelinePushPayload("weather", weather);
//        sendText(session, JsonUtils.toJson(payload));
//    }
//
//    private void sendVehicleEvents(Session session) throws Exception {
//        List<VehicleEvent> events = getMockVehicleEvents();
//        int lastTime = 0;
//        for (VehicleEvent event : events) {
//            int delay = event.startTime - lastTime;
//            if (delay > 0) Thread.sleep(delay * 1000L);
//
//            TimelinePushPayload payload = new TimelinePushPayload("event", event);
//            sendText(session, JsonUtils.toJson(payload));
//
//            lastTime = event.startTime;
//        }
//    }
//
//    private void sendText(Session session, String text) throws Exception {
//        synchronized (session) {
//            session.getBasicRemote().sendText(text);
//        }
//    }
//
//    // ==================== Mock Data =====================
//
//    private List<VehicleEvent> getMockVehicleEvents() {
//        return List.of(
//                new VehicleEvent("消防车2", "出动行驶中", List.of("李玲玉", "张祥", "王凡"), 0, 15),
//                new VehicleEvent("消防车2", "到达险情点", List.of("李玲玉", "张祥", "王凡"), 15, 16),
//                new VehicleEvent("消防车2", "执行任务中", List.of("李玲玉", "张祥", "王凡"), 16, 35),
//                new VehicleEvent("消防车2", "回停行驶中", List.of("李玲玉", "张祥", "王凡"), 36, 55),
//                new VehicleEvent("消防车2", "任务已完成", List.of("李玲玉", "张祥", "王凡"), 55, 56)
//        );
//    }
//
//    // ==================== Data Models =====================
//
//    @Data
//    static class WeatherInfo {
//        private final String windDirection;
//        private final int windSpeed;
//    }
//
//    @Data
//    static class VehicleEvent {
//        private final String vehicleName;
//        private final String statusText;
//        private final List<String> personnel;
//        private final int startTime;
//        private final int endTime;
//    }
//
//    @Data
//    static class TimelinePushPayload {
//        private final String type; // "weather" or "event"
//        private final Object data;
//    }
//
//    // ==================== Utils (Simulated) =====================
//
//    static class JsonUtils {
//        public static String toJson(Object obj) {
//            // 替换成你自己的 JSON 序列化逻辑（如 Jackson）
//            return "[MOCK_JSON] " + obj.toString();
//        }
//    }
//}
