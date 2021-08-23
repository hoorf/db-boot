package org.github.hoorf.dbboot.common;

import java.util.concurrent.atomic.AtomicInteger;

public class StateCtrl {

    public static final Integer STATE_INIT = 0;
    public static final Integer STATE_BREAK = 4;
    public static final Integer STATE_PENDING = 3;
    public static final Integer STATE_RUNNING = 2;
    public static final Integer STATE_FINISH = 1;
    ;

    protected AtomicInteger STATE = new AtomicInteger(0);

    public boolean isFinished() {
        return STATE.get() == STATE_FINISH || STATE.get() == STATE_BREAK;
    }

    public boolean isRunning() {
        return !isFinished();
    }

    protected boolean setState(Integer expect, Integer update) {
        return STATE.compareAndSet(expect, update);
    }

    protected void forceState(Integer update) {
        STATE.set(update);
    }

    protected void fastFail() {
        forceState(STATE_BREAK);
    }

    protected void fastFinish() {
        forceState(STATE_FINISH);
    }


}
