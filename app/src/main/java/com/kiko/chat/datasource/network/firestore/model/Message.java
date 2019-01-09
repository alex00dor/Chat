package com.kiko.chat.datasource.network.firestore.model;

import java.util.HashMap;
import java.util.Map;

public class Message extends com.kiko.chat.domain.model.Message {
    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("message", getMassage());
        map.put("sender", getSender());
        map.put("receiver", getReceiver());
        map.put("read", isRead());
        map.put("image", isImage());
        map.put("timestamp", getTimestamp());
        return map;
    }
}
