package org.sid.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Deaths {
    @Id
    private String id;
    private String state;
    private String country;
    private int latestDeathsCases;
    private int diffFromPrevDay;
}
