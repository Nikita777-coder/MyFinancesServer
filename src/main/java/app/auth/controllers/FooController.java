package app.auth.controllers;

import app.auth.dto.FooMarketStock;
import app.auth.dto.FooUserStock;
import app.auth.servicies.FooService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foo-debug-funcs")
@RequiredArgsConstructor
public class FooController {
    private final FooService fooService;
    @PostMapping("/save-user-stocks")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveUserStocks(@RequestBody List<FooUserStock> fooUserStocks, @RequestParam("email") String email) {
        fooService.saveUserStocks(fooUserStocks, email);
    }
    @GetMapping("/get-user-stocks")
    @ResponseStatus(HttpStatus.OK)
    public List<FooUserStock> getUserStocks(@RequestParam("email") String email) {
        return fooService.getUserStocks(email);
    }
    @PostMapping("/save-market-stocks")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveMarketStocks(@RequestBody List<FooMarketStock> fooMarketStocks) {
        fooService.saveMarketStocks(fooMarketStocks);
    }
    @GetMapping("/get-market-stocks")
    @ResponseStatus(HttpStatus.OK)
    public List<FooMarketStock> getMarketStocks() {
        return fooService.getMarketStocks();
    }
}
