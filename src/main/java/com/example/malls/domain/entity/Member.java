package com.example.malls.domain.entity;

import com.example.malls.global.auditable.Auditable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
public class Member extends Auditable{
    @Id
    private String mid;

    private String mpw;
    private String email;
    private String nickname;
    private boolean del;

    private boolean social;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void changeEmail(String email){
        this.email=email;
    }

    public void changePassword(String mpw){
        this.mpw=mpw;
    }

    public void changeDel(boolean del){
        this.del=del;
    }
    public void clearRole(MemberRole memberRole){
        this.roleSet.add(memberRole);
    }

    public void addRole(MemberRole memberRole){
        this.roleSet.add(memberRole);
    }

    public void clearRole(){
        this.roleSet.clear();
    }

    public void changeSocial(boolean social){
        this.social=social;
    }
}
