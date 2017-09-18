package com.yzz.sevenp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 */

public class UpDateStatus implements Serializable {

    /**
     * result : [{"created_at":1504074775021,"updated_at":1504077556401,"id":1409842729254944,"data":{"live_name":"1406631737884679","pic":"https://raw.githubusercontent.com/DyncKathline/LiveGiftLayout/master/giftlibrary/src/main/assets/p/000.png","live_type":0,"status":0},"uid":1406631737884679}]
     * error_code : 0
     */

    private int error_code;
    private List<ResultBean> result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * created_at : 1504074775021
         * updated_at : 1504077556401
         * id : 1409842729254944
         * data : {"live_name":"1406631737884679","pic":"https://raw.githubusercontent.com/DyncKathline/LiveGiftLayout/master/giftlibrary/src/main/assets/p/000.png","live_type":0,"status":0}
         * uid : 1406631737884679
         */

        private long created_at;
        private long updated_at;
        private long id;
        private DataBean data;
        private long uid;

        public long getCreated_at() {
            return created_at;
        }

        public void setCreated_at(long created_at) {
            this.created_at = created_at;
        }

        public long getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(long updated_at) {
            this.updated_at = updated_at;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public static class DataBean {
            /**
             * live_name : 1406631737884679
             * pic : https://raw.githubusercontent.com/DyncKathline/LiveGiftLayout/master/giftlibrary/src/main/assets/p/000.png
             * live_type : 0
             * status : 0
             */

            private String live_name;
            private String pic;
            private int live_type;
            private int status;

            public String getLive_name() {
                return live_name;
            }

            public void setLive_name(String live_name) {
                this.live_name = live_name;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public int getLive_type() {
                return live_type;
            }

            public void setLive_type(int live_type) {
                this.live_type = live_type;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
