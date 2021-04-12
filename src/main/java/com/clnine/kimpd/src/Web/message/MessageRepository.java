package com.clnine.kimpd.src.Web.message;

import com.clnine.kimpd.src.Web.message.models.Message;
import com.clnine.kimpd.src.Web.user.models.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message,Integer> {
    Message findByMessageIdxAndStatus(int messageIdx,String status);
    List<Message> findByReceiverAndStatus(UserInfo userInfo, String status, Pageable pageable);
    int countAllByReceiverAndStatus(UserInfo userInfo,String status);
}
