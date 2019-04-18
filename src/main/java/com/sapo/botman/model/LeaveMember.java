package com.sapo.botman.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "leave_member")
public class LeaveMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private MemberJOB memberJOB;
    @Temporal(TemporalType.DATE)
    private Date dateLeave;
    private String typeLeave;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created", updatable = false)
    private Date created;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MemberJOB getMemberJOB() {
        return memberJOB;
    }

    public void setMemberJOB(MemberJOB memberJOB) {
        this.memberJOB = memberJOB;
    }

    public Date getDateLeave() {
        return dateLeave;
    }

    public void setDateLeave(Date dateLeave) {
        this.dateLeave = dateLeave;
    }

    public String getTypeLeave() {
        return typeLeave;
    }

    public void setTypeLeave(String typeLeave) {
        this.typeLeave = typeLeave;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}
