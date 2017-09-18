package com.yzz.sevenp.bean;

/**
 * Created by yg on 2017/8/28.
 */

public class Login {

    /**
     * result : {"created_at":1503909298340,"updated_at":1503909298362,"id":1407066485882902,"user_data":{"phone":"13593420402","user_name":"mcduang","avatar":"http://p4.so.qhmsg.com/t01191654eb345ad3a9.jpg","sign":"啧啧啧啧啧"}}
     * error_code : 0
     */

    private ResultBean result;
    private int error_code;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        /**
         * created_at : 1503909298340
         * updated_at : 1503909298362
         * id : 1407066485882902
         * user_data : {"phone":"13593420402","user_name":"mcduang","avatar":"http://p4.so.qhmsg.com/t01191654eb345ad3a9.jpg","sign":"啧啧啧啧啧"}
         */

        private long created_at;
        private long updated_at;
        private long id;
        private UserDataBean user_data;

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

        public UserDataBean getUser_data() {
            return user_data;
        }

        public void setUser_data(UserDataBean user_data) {
            this.user_data = user_data;
        }

        public static class UserDataBean {
            /**
             * phone : 13593420402
             * user_name : mcduang
             * avatar : http://p4.so.qhmsg.com/t01191654eb345ad3a9.jpg
             * sign : 啧啧啧啧啧
             */

            private String phone;
            private String user_name;
            private String avatar;
            private String sign;

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }
        }
    }
}
