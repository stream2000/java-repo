package org.example.nettyDemo.Common.Proto;

import org.example.nettyDemo.Common.ProtoRouter.ProtoRouter;

public class RouterInitializer {
public static ProtoRouter initialize() {
    ProtoRouter router = new ProtoRouter();
    router.register((byte) ProtobufOuter.MessageType.UserSendRequestType.getNumber(), ProtobufOuter.UserSendRequest.getDefaultInstance());
    router.register((byte) ProtobufOuter.MessageType.UserSendResponseType.getNumber(), ProtobufOuter.UserSendResponse.getDefaultInstance());
    return router;
}
}
