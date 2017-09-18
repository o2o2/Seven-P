package com.yzz.sevenp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */
public class MyLive implements Serializable{
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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * created_at : 1504173951896
             * updated_at : 1504174100599
             * id : 1411506626429011
             * data : {"status":1,"live_type":1,"pic":"https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1380084653,2448555822&fm=26&gp=0.jpg","live_name":"1公顷"}
             * uid : 1401277322952706
             * user : {"user_data":{"user_name":"我是好人","avatar":"http://s.click.taobao.com/t?spm=a231o.7076277.20003.17.460772c1TEDFle&prepvid=200_11.224.194.140_180_1503563809792&extra=&e=m%3D2%26s%3D36GGw%2B7%2BVUUcQipKwQzePOeEDrYVVa64REOHN%2B0iJT23bLqV5UHdqVMWpc1y3BPKyqeBTj9FJSgodFpjPZa1TogTRp0Frj9clFHbSVAJ0l1LlGw1d2T0eh96NmP5jASVg22ZWy%2BSE6TxXb80WZZ4gEpiTOHXdqkM1emRhX8jWhApkzvCLmZ8jw4cNnIzXNCN9Tg3eG7Qhv%2FGJe8N%2FwNpGw%3D%3D&amp;&pvid=200_11.224.194.140_180_1503563809792","sign":"打法菲利克斯","phone":"15835852869"},"id":1401277322952706,"created_at":1503564237292,"updated_at":1503564237314}
             */

            private long created_at;
            private long updated_at;
            private long id;
            private DataBean data;
            private long uid;
            private UserBean user;

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

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
            }

            public static class DataBean {
                /**
                 * status : 1
                 * live_type : 1
                 * pic : https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1380084653,2448555822&fm=26&gp=0.jpg
                 * live_name : 1公顷
                 */

                private int status;
                private int live_type;
                private String pic;
                private String live_name;

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getLive_type() {
                    return live_type;
                }

                public void setLive_type(int live_type) {
                    this.live_type = live_type;
                }

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }

                public String getLive_name() {
                    return live_name;
                }

                public void setLive_name(String live_name) {
                    this.live_name = live_name;
                }
            }


        }

        public static class UserBean {
            /**
             * user_data : {"user_name":"我是好人","avatar":"http://s.click.taobao.com/t?spm=a231o.7076277.20003.17.460772c1TEDFle&prepvid=200_11.224.194.140_180_1503563809792&extra=&e=m%3D2%26s%3D36GGw%2B7%2BVUUcQipKwQzePOeEDrYVVa64REOHN%2B0iJT23bLqV5UHdqVMWpc1y3BPKyqeBTj9FJSgodFpjPZa1TogTRp0Frj9clFHbSVAJ0l1LlGw1d2T0eh96NmP5jASVg22ZWy%2BSE6TxXb80WZZ4gEpiTOHXdqkM1emRhX8jWhApkzvCLmZ8jw4cNnIzXNCN9Tg3eG7Qhv%2FGJe8N%2FwNpGw%3D%3D&amp;&pvid=200_11.224.194.140_180_1503563809792","sign":"打法菲利克斯","phone":"15835852869"}
             * id : 1401277322952706
             * created_at : 1503564237292
             * updated_at : 1503564237314
             */

            private UserDataBean user_data;
            private long id;
            private long created_at;
            private long updated_at;

            public UserDataBean getUser_data() {
                return user_data;
            }

            public void setUser_data(UserDataBean user_data) {
                this.user_data = user_data;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

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

            public static class UserDataBean {
                /**
                 * user_name : 我是好人
                 * avatar : http://s.click.taobao.com/t?spm=a231o.7076277.20003.17.460772c1TEDFle&prepvid=200_11.224.194.140_180_1503563809792&extra=&e=m%3D2%26s%3D36GGw%2B7%2BVUUcQipKwQzePOeEDrYVVa64REOHN%2B0iJT23bLqV5UHdqVMWpc1y3BPKyqeBTj9FJSgodFpjPZa1TogTRp0Frj9clFHbSVAJ0l1LlGw1d2T0eh96NmP5jASVg22ZWy%2BSE6TxXb80WZZ4gEpiTOHXdqkM1emRhX8jWhApkzvCLmZ8jw4cNnIzXNCN9Tg3eG7Qhv%2FGJe8N%2FwNpGw%3D%3D&amp;&pvid=200_11.224.194.140_180_1503563809792
                 * sign : 打法菲利克斯
                 * phone : 15835852869
                 */

                private String user_name;
                private String avatar;
                private String sign;
                private String phone;

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

                public String getPhone() {
                    return phone;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
                }
            }
        }
}

}
