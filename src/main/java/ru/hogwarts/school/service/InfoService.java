package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class InfoService {
    @Value("${server.port}")
    private int port;

    public int getPort() {
        return port;
    }

    public String getSum(Long limit) {
        long start = System.currentTimeMillis();
        Long sum = Stream.iterate(1L, a -> a + 1).limit(limit).reduce(0L, (a, b) -> a + b);
        System.out.println(System.currentTimeMillis() - start);
        long start2 = System.currentTimeMillis();
        Long sum2 = Stream.iterate(1L, a -> a + 1).limit(limit).parallel().reduce(0L, Long::sum, Long::sum);
        System.out.println(System.currentTimeMillis() - start2);
        //замер времени показал что выполняются одинаково в пределах погрешности 2 способ выполняется дольше на 10--15 мс
        //При увеличении количетсва элементов во 2 способе вылетает ошибка.OutOfMemoryError
//именно в таком виде этот поток лучше заменить на формулу суммы арифметической прогрессии
        long start3 = System.currentTimeMillis();
        long element1 = 1;
        Long sum3 = (limit + element1) * limit / 2;
        System.out.println(System.currentTimeMillis() - start3);

        long start4 = System.currentTimeMillis();
        Long sum4= Stream.iterate(1L, a -> a + 1).limit(limit).parallel().reduce(0L, (a,b)->{
            long s=0;
            for(int i=0;i<(a+b);i++){
              s+=1; }return s;
        });
        //
        // для лимита 3 создано 6 лонг переменных и 6 инт переменных(при этом одновременно по 3  по количеству выделенных для выполнения потоков)
        System.out.println(System.currentTimeMillis() - start4);

        return sum + "  " + sum2 + "  " + sum3;
    }
}
