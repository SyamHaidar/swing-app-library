package service;

import model.Member;
import repository.MemberRepository;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean dataExist(String id) {
        return memberRepository.dataExist(id);
    }

    @Override
    public String getAllData() {
        return memberRepository.getAllData();
    }

    @Override
    public void getData(String id) {
        memberRepository.getData(id);
    }

    @Override
    public void add(Member member) {
        memberRepository.add(member);
    }

    @Override
    public void edit(String id, Member member) {
        memberRepository.edit(id, member);
    }

    @Override
    public void update(String id, Member member) {
        memberRepository.update(id, member);
    }

    @Override
    public void remove(String id) {
        memberRepository.remove(id);
    }
}
