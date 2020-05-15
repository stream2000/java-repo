package org.example.nettyDemo.Common.ProtoRouter;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

public interface Handler {
void handle(ChannelHandlerContext channelHandlerContext, GeneratedMessageV3 message) throws Exception;
}
