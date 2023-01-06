package service;

import model.Member;

public interface MemberService {

    boolean dataExist(String id);

    String getAllData();

    void getData(String id);

    void add(Member member);

    void edit(String id, Member member);

    void update(String id, Member member);

    void remove(String id);

}
