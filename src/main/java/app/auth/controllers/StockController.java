package app.auth.controllers;

import app.auth.dto.foo.MarketStock;
import app.auth.dto.foo.UserPortfolioRisk;
import app.auth.dto.foo.UserStock;
import app.auth.servicies.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foo-debug-funcs")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @PostMapping("/save-user-stocks")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveUserStocks(@RequestBody List<UserStock> userStocks, @RequestParam("email") String email) {
        stockService.saveUserStocks(userStocks, email);
    }

    @GetMapping("/get-user-stocks")
    @ResponseStatus(HttpStatus.OK)
    public List<UserStock> getUserStocks(@RequestParam("email") String email) {
        return stockService.getUserStocks(email);
    }

    @PostMapping("/save-market-stocks")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveMarketStocks(@RequestBody List<MarketStock> marketStocks) {
        stockService.saveMarketStocks(marketStocks);
    }

    @GetMapping("/get-market-stocks")
    @ResponseStatus(HttpStatus.OK)
    public List<MarketStock> getMarketStocks() {
        return stockService.getMarketStocks();
    }

    @PostMapping("/save-user-risk")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveUserRisk(@RequestBody UserPortfolioRisk userPortfolioRisk) {
        stockService.saveUserPortfolioRisk(userPortfolioRisk);
    }

    @GetMapping("/get-user-risk")
    @ResponseStatus(HttpStatus.OK)
    public UserPortfolioRisk getUserRisk(@RequestParam("email") String email) {
        return stockService.getUserPortfolioRisk(email);
    }
}
