package org.example.nettyDemo.Producer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.nettyDemo.Common.Proto.ProtobufOuter;

public class ClientHandler extends SimpleChannelInboundHandler<ProtobufOuter.UserSendResponse> {
@Override
protected void channelRead0(ChannelHandlerContext channelHandlerContext, ProtobufOuter.UserSendResponse response) throws Exception {
    if( response.getError() != ProtobufOuter.UserSendResponse.mq_error.OK){
        System.out.println("error response:" + response);
    }else {
        System.out.println("response with id  "+response.getLetterId());
    }
}
}
