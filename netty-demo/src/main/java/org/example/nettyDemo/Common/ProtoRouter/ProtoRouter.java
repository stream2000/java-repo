package org.example.nettyDemo.Common.ProtoRouter;

import com.google.protobuf.GeneratedMessageV3;

import java.util.HashMap;
import java.util.Map;


public class ProtoRouter {
private Map<Byte, GeneratedMessageV3> typeMessageMap = new HashMap<>();
private Map<Class, Byte> messageTypeMap = new HashMap<>();
private Map<Byte, Handler> handlerMap = new HashMap<>();

public void register(byte messageType, GeneratedMessageV3 message) {
    typeMessageMap.put(messageType, message);
    messageTypeMap.put(message.getClass(), messageType);
}

public boolean registerHandler(GeneratedMessageV3 msg, Handler handler) {
    Byte messageType = messageTypeMap.get(msg.getClass());
    if (!typeMessageMap.containsKey(messageType)) {
        return false;
    }
    handlerMap.put(messageType, handler);
    return true;
}

public Map<Byte, GeneratedMessageV3> getTypeMessageMap() {
    return typeMessageMap;
}

public Map<Class, Byte> getMessageTypeMap() {
    return messageTypeMap;
}

public Handler getHandler(Object obj) {
    Byte messageType = messageTypeMap.get(obj.getClass());
    return handlerMap.get(messageType);
}
public Handler getHandler(Byte messageType) {
    return handlerMap.get(messageType);
}
}
