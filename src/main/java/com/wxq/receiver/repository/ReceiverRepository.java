package com.wxq.receiver.repository;

import com.wxq.receiver.repository.po.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiverRepository extends JpaRepository<Record, Integer> {

}
