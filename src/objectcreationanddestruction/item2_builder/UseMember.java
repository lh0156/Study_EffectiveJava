package objectcreationanddestruction.item2_builder;

public class UseMember {

    public static void main(String[] args) {


        Member member = new Member.Builder().name("엄윤섭").address("신림").tel("010-0000-0000").build();

        System.out.println(member.getName());
        System.out.println(member.getAddress());
        System.out.println(member.getTel());
    }

}
