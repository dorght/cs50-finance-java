package net.cs50.finance.controllers;

import net.cs50.finance.models.Stock;
import net.cs50.finance.models.StockHolding;
import net.cs50.finance.models.StockLookupException;
import net.cs50.finance.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Chris Bay on 5/17/15.
 */
@Controller
public class PortfolioController extends AbstractFinanceController {

    @RequestMapping(value = "/portfolio")
    public String portfolio(HttpServletRequest request, Model model){
        HashMap<String, HashMap> formatedportfolio = new HashMap<>();

        // DONE TODO - Implement portfolio display
        User user = getUserFromSession(request);
        Map<String, StockHolding> portfolio = user.getPortfolio();
        Set<String> stocks = portfolio.keySet();
        double equity = 0.0;

        for (String symbol : stocks) {
            StockHolding stock = portfolio.get(symbol);

            Stock stocklookup;
            try {
                stocklookup = Stock.lookupStock(symbol);
            } catch (StockLookupException e) {
                e.printStackTrace();
                return displayError("Unable to lookup up stocks to display in portfolio.", model);
            }

            double total = stocklookup.getPrice() * stock.getSharesOwned();
            String totalstr = String.format("$%.2f", total);
            equity += total;

            HashMap<String, String> formatedstock = new HashMap<>();
            formatedstock.put("symbol", stocklookup.getSymbol());
            formatedstock.put("name", stocklookup.getName());
            formatedstock.put("shares", String.format("%d", stock.getSharesOwned()));
            formatedstock.put("price", String.format("$%.2f", stocklookup.getPrice()));
            formatedstock.put("total", totalstr);

            formatedportfolio.put(stocklookup.getSymbol(), formatedstock);
        }

        String cashstr = String.format("$%.2f",user.getCash());

        equity += user.getCash();
        String equitystr = String.format("$%.2f", equity);

        model.addAttribute("stocks", formatedportfolio);
        model.addAttribute("cash", cashstr);
        model.addAttribute("equity", equitystr);
        model.addAttribute("title", "Portfolio");
        model.addAttribute("portfolioNavClass", "active");

        return "portfolio";
    }

}
