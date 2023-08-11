package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.LifecycleState;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer sx;//起始x坐标
    private Integer sy;//起始y坐标
    private List<Integer>steps;//保存每一步操作--决定了蛇当前的形状
}
