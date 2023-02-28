package objectcreationanddestruction.item2_builder;

public class Member {

    String name;
    String address;
    String tel;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTel() {
        return tel;
    }

    public Member(Builder builder) {
        this.name = builder.name;
        this.address = builder.address;
        this.tel = builder.tel;
    }

    public static class Builder {

        private String name;
        private String address;
        private String tel;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder tel(String tel) {
            this.tel = tel;
            return this;
        }

        public Member build() {
            return new Member(this);
        }

    }

}
