package com.jvmdevelop.strife.controllers;

import com.jvmdevelop.strife.controller.WebRtcHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebRtcHandlerTest {

    @Mock
    private KurentoClient kurentoClient;

    @Mock
    private MediaPipeline mediaPipeline;

    @Mock
    private WebRtcEndpoint webRtcEndpoint;

    @Mock
    private WebSocketSession session;

    @InjectMocks
    private WebRtcHandler webRtcHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(kurentoClient.createMediaPipeline()).thenReturn(mediaPipeline);
        when(new WebRtcEndpoint.Builder(mediaPipeline).build()).thenReturn(webRtcEndpoint);
    }

    @Test
    void afterConnectionEstablished_shouldCreateMediaPipelineAndWebRtcEndpoint() throws Exception {
        when(session.getAttributes()).thenReturn(Map.of("channel", "testChannel", "user", "testUser"));

        webRtcHandler.afterConnectionEstablished(session);

        verify(kurentoClient, times(1)).createMediaPipeline();
        verify(new WebRtcEndpoint.Builder(mediaPipeline).build(), times(1));
        assertNotNull(getChannels().get("testChannel"));
        assertNotNull(getUsers().get("testChannel").get("testUser"));
    }

    @Test
    void handleTextMessage_shouldProcessOfferAndSendSdpAnswer() throws Exception {
        when(session.getAttributes()).thenReturn(Map.of("channel", "testChannel", "user", "testUser"));
        when(webRtcEndpoint.processOffer(anyString())).thenReturn("sdpAnswer");

        getChannels().put("testChannel", mediaPipeline);
        getUsers().put("testChannel", new ConcurrentHashMap<>(Map.of("testUser", webRtcEndpoint)));

        webRtcHandler.handleTextMessage(session, new TextMessage("offer"));

        verify(webRtcEndpoint, times(1)).processOffer("offer");
        verify(session, times(1)).sendMessage(new TextMessage("sdpAnswer"));
    }

    @Test
    void afterConnectionClosed_shouldReleaseWebRtcEndpointAndMediaPipeline() throws Exception {
        when(session.getAttributes()).thenReturn(Map.of("channel", "testChannel", "user", "testUser"));

        getChannels().put("testChannel", mediaPipeline);
        getUsers().put("testChannel", new ConcurrentHashMap<>(Map.of("testUser", webRtcEndpoint)));

        webRtcHandler.afterConnectionClosed(session, null);

        verify(webRtcEndpoint, times(1)).release();
        verify(mediaPipeline, times(1)).release();
        assertNull(getChannels().get("testChannel"));
        assertNull(getUsers().get("testChannel"));
    }

    @Test
    void connectPeers_shouldConnectAllUsersInChannel() {
        WebRtcEndpoint endpoint1 = mock(WebRtcEndpoint.class);
        WebRtcEndpoint endpoint2 = mock(WebRtcEndpoint.class);

        ConcurrentMap<String, WebRtcEndpoint> channelUsers = new ConcurrentHashMap<>();
        channelUsers.put("user1", endpoint1);
        channelUsers.put("user2", endpoint2);

        getUsers().put("testChannel", channelUsers);

        webRtcHandler.connectPeers("testChannel");

        verify(endpoint1, times(1)).connect(endpoint2);
        verify(endpoint2, times(1)).connect(endpoint1);
    }

    @Test
    void getChannelInfo_shouldReturnChannelInfo() {
        WebRtcEndpoint endpoint = mock(WebRtcEndpoint.class);

        ConcurrentMap<String, WebRtcEndpoint> channelUsers = new ConcurrentHashMap<>();
        channelUsers.put("user1", endpoint);

        getUsers().put("testChannel", channelUsers);

        String info = webRtcHandler.getChannelInfo("testChannel");

        assertEquals("Users in channel testChannel:\nuser1\n", info);
    }

    @Test
    void getChannelInfo_shouldReturnChannelNotFound() {
        String info = webRtcHandler.getChannelInfo("nonExistentChannel");

        assertEquals("Channel not found.", info);
    }

    private ConcurrentMap<String, MediaPipeline> getChannels() {
        return WebRtcHandler.channels;
    }

    private ConcurrentMap<String, ConcurrentMap<String, WebRtcEndpoint>> getUsers() {
        return WebRtcHandler.users;
    }
}