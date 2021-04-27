package com.wxq.receiver.repository.po;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "filerecord")
public class Record {
    @Id
    private Long id;

    private Date time;

    private String filename;
}
