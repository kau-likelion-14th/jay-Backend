package likelion14th.lte.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter // private 변수? 함수를 만들어줌
public abstract class BaseEntity {

    @CreationTimestamp // 생성되는 시간 감지
    @Column // column이 될거라고 알려줌
    private LocalDateTime createdAt; // created_at 으로 변환되서 들어감(대문자 안됨)

    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updatedAt;

}
