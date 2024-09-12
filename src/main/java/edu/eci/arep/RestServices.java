package edu.eci.arep;

@RestController
public class RestServices {
    @GetMapping("/")
    public String getCalculadora() {
        return "index.html";
    }

    @GetMapping("/calculadora/PI")
    public Double getPI() {
        System.out.println(Math.PI);
        return Math.PI;

    }
}
