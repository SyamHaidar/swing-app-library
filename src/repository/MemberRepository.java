package repository;

import model.Member;

public interface MemberRepository {

    boolean dataExist(String id);

    String getAllData();

    void getData(String id);

    String getTopMember();

    String getTopLateMember();

    void add(Member member);

    void edit(String id, Member member);

    void update(String id, Member member);

    void remove(String id);

}
