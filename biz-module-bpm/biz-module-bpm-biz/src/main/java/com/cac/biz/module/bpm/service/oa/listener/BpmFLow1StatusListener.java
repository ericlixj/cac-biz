//package com.cac.biz.module.bpm.service.oa.listener;
//
//import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
//import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
//import org.flowable.common.engine.api.delegate.event.FlowableEvent;
//import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
//import org.flowable.engine.HistoryService;
//import org.flowable.engine.runtime.ProcessInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class BpmFLow1StatusListener implements FlowableEventListener {
//
//
//    @Override
//    public void onEvent(FlowableEvent event) {
//        if (FlowableEngineEventType.PROCESS_COMPLETED.equals(event.getType())) {
//            // 将事件转换为 FlowableEntityEvent
//            FlowableEntityEvent entityEvent = (FlowableEntityEvent) event;
//
//            // 获取流程实例
//            ProcessInstance processInstance = (ProcessInstance) entityEvent.getEntity();
//            String processInstanceId = processInstance.getId();
//
//        }
//    }
//
//    @Override
//    public boolean isFailOnException() {
//        return false; // 如果监听器中出现异常，流程不会中断
//    }
//
//    @Override
//    public boolean isFireOnTransactionLifecycleEvent() {
//        return false;
//    }
//
//    @Override
//    public String getOnTransaction() {
//        return null;
//    }
//}
