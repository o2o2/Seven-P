package com.yzz.sevenp.bean;

import java.io.Serializable;

/**
 * Created by Wookeibun on 2017/8/25.
 */

public class Register implements Serializable {
    /**
     * result : true
     * error_code : 0
     */

    private boolean result;
    private int error_code;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
}
