package com.qiwenshare.stock.analysis.indicator;

import com.qiwenshare.stock.analysis.TechnologyAnalysis;
import com.qiwenshare.stock.domain.ReplayBean;

import java.util.ArrayList;
import java.util.List;

public abstract class IndicatorAnalysis extends TechnologyAnalysis {
    List<ReplayBean> replayBeanList = new ArrayList<ReplayBean>();
}
