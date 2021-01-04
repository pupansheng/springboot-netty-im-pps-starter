package com.pps;

import io.netty.channel.ChannelPipeline;

import java.util.function.Consumer;

public interface PpsNettyImChannelPipelineCostom {

     Consumer<ChannelPipeline> custommChannelPipeline();

}
