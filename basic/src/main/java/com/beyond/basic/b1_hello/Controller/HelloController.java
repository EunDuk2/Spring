package com.beyond.basic.b1_hello.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// Component 어노테이션을 통해 별도의 객체를 생성할 필요가 없는, 싱글톤 객체 생성
// Controller 어노테이션을 통해 쉽게 사용자의 http request를 분석하고, http response를 생성
@Controller
// 클래스 차원의 url 매핑 시에는 RequestMapping을 사용한다.
@RequestMapping("/hello")
public class HelloController {
    // get 요쳥의 case들
    // case1. 서버가 사용자에게 단순 String 데이터 return - @ResponseBody 있을 때
    @GetMapping("") // 아래 메서드에 대한  서버의 end 포인트를 설정
    // ResponseBody가 없고, return 타입이 String인 경우 서버는 templates 폴더 밑에 helloworld.html을 찾아서 리턴
    // 우리는 서버에서 화면을 만들지는 않음
    @ResponseBody
    public String helloWorld() {
        return "helloworld";
    }

    // case2. 서버가 사용자에게 String(json형식)의 데이터 return
    @GetMapping("/json")
    @ResponseBody
    public Hello helloJson() throws JsonProcessingException {
        Hello h1 = new Hello("hong", "hong@naver.com");
        // 직접 json으로 직렬화 할 필요 없이, return 타입에 객체가 있으면 자동으로 직렬화된다.
//        ObjectMapper objectMapper = new ObjectMapper();
//        String result = objectMapper.writeValueAsString(h1);

        return h1;
    }

    // case3. parameter 방식을 통해 사용자로부터 값을 수신
    // parameter의 형식 : /member?id=1 , /member?name=hong
    @GetMapping("/param")
    @ResponseBody
    public Hello param(@RequestParam(value = "name")String inputName) {
        return new Hello(inputName, "amuguna@naver.com");
    }

    // case4. pathvariable 방식을 통해 사용자로부터 값을 수신
    // pathvariable의 형식 : /member/1 (?id=1 보다 명확한 의미)
    // pathvariable 방식은 url을 통해 자원의 구조를 명확하게 표현할 때 사용 (좀 더 RestFul 함)
    @GetMapping("/path/{inputId}")
    @ResponseBody
    public String path(@PathVariable Long inputId) {
        // 별도의 형변환 없이도, 매개변수에 타입지정 시 자동 형 변환 시켜줌.
//        long id = Long.parseLong(inputId);
//        System.out.println(id);
        System.out.println(inputId);
        return "OK";
    }

    // case 5. parameter 2개 이상 형식
    // /hello/param2?name=hongildong&email=hong@naver.com
    // 이 때는 pathvariable로 처리하기에는 한계가 있음
    @GetMapping("/param2")
    @ResponseBody
    public String param2(@RequestParam(value = "name")String inputName, @RequestParam(value = "email")String inputEmail) {
        System.out.println(inputName + " " + inputEmail);

        return "ok";
    }
}
