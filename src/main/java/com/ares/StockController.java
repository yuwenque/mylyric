package com.ares;

import com.ares.entity.stock.Stock925;
import com.ares.entity.stock.StockAdd;
import com.ares.entity.stock.StockListBody;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Query;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/")
public class StockController {


    @Autowired
    StockService service;

    @RequestMapping(value = "/add_stock_list",method = {RequestMethod.POST})
    public Map<String,Object> addStockList(@RequestBody StockListBody listBody){

        Map<String,Object> response = new HashMap<>();
        if(listBody!=null){
            List<StockAdd> list = listBody.getList();
            if(list!=null&&list.size()>0){
                for (StockAdd stockAdd : list) {
                    service.insertStock(stockAdd);
                }
            }
        }
        response.put("code",200);
        response.put("msg","添加成功");
        return response;
    }

    @RequestMapping("/query925stocks")
    public  List<Map<String,Object>> query925stocks(){

        List<Map<String,Object>> response = new ArrayList<>();
        //获取昨天的日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String todayTime = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        todayTime=todayTime+","+"09:25";
        calendar.add(Calendar.DATE,-1);
        Date yesterday = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String yesterdayTime = df.format(yesterday);

       List<StockAdd> stockYesList =  service.queryAddStock(yesterdayTime);

        for (StockAdd stockYes : stockYesList) {
            String id = stockYes.getId();
            Stock925 stock925 = service.checkStock925(id,todayTime);
            if(stock925==null){
                stock925 =  getStock925onTime(id);
            }
            if(stock925!=null){
                Map<String,Object> map = new HashMap<>();

                double percent = stock925.getCallAuction()/(stockYes.getVol_on_up()*100);
                map.put("id",stock925.getId());
                map.put("name",stock925.getName());
                map.put("create_time",stock925.getCreateTime());
                map.put("vol_on_up",stockYes.getVol_on_up());
                map.put("percent",percent);
                map.put("call_auction",stock925.getCallAuction());

                response.add(map);
            }



        }

        return response;
    }

    public static void main(String args[]){

        //获取昨天的日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,-1);
        Date yesterday = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = df.format(yesterday);
        System.out.println(dateString);
        getStock925onTime("000023");

    }

    private static Stock925 getStock925onTime(String id){
        Stock925 stock925  = new Stock925();

        String index_url = "http://hq.sinajs.cn/list=%s%s";

        char[] chars= id.toCharArray();
        char startChar = chars[0];
        String url = "";
        if(startChar=='3'||startChar=='0'||startChar=='1'||startChar=='2'){
            url = String.format(index_url,"sz",id);
        }else if(startChar=='6'){
            url = String.format(index_url,"sh",id);
        }

        System.out.println("开始请求："+url);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        OkHttpClient client =  builder.build();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = client.newCall(request);
        try {
            Response rs = call.execute();
            String content = rs.body().string();
            int indexOfD = content.indexOf("=")+2;
            content = content.substring(indexOfD,content.length()).replace("\";","");
           String arr []=  content.split(",");
            String name  = arr[0];
            System.out.println(name);
            int call_auction = Integer.parseInt(arr[8]);
            System.out.println(call_auction);
            String create_date = arr[arr.length-3];
            String create_minute = arr[arr.length-2];
            String ms[] = create_minute.split(":");
            create_minute = ms[0]+":"+ms[1];
            String timeStamp = create_date+","+create_minute;
            System.out.println(timeStamp);
            System.out.println("请求结果："+content);
            if("09:25".equals(create_minute)){
                stock925.setId(id);
                stock925.setName(name);
                stock925.setCallAuction(call_auction);
                stock925.setCreateTime(timeStamp);
            }else{

                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return stock925;

    }


    @RequestMapping("/addStock")
    public Map<String,Object> addStock(@RequestParam("id")String id, @RequestParam("name")String name, @RequestParam("vol")long vol, @RequestParam("create_time")String create_time){

        Map<String,Object> response = new HashMap<>();
        if(id!=null&&name!=null&&vol>0&&create_time!=null){

            StockAdd stockAdd = new StockAdd();
            stockAdd.setId(id);
            stockAdd.setName(name);
            stockAdd.setVol_on_up(vol);
            stockAdd.setCreateTime(create_time);

           int result =  service.insertStock(stockAdd);
            if(result>0){

                response.put("code",200);
                response.put("msg","添加成功");
            }else{
                response.put("code",-1);
                response.put("msg","添加失败");
            }

        }else{

            response.put("code",-100);
            response.put("msg","缺少参数");
        }


        return response;
    }

    @RequestMapping("/queryTodayStocks")
    public List<StockAdd> queryAddStocks(@RequestParam("date")String date){

        return service.queryAddStock(date);


    }
    @RequestMapping("/queryStocks")
    public List<StockAdd> queryStocks(@RequestParam("date")String date){




        return service.queryAddStock(date);


    }

    @RequestMapping("/deleteTodayAddStock")
    public  Map<String,Object> deleteAddStock(@RequestParam("id")String id){
        Map<String,Object> response = new HashMap<>();


        service.deleteStockAdd(id);
        response.put("code",200);
        response.put("msg","删除成功");

        return response;

    }

    @RequestMapping("/check/{id}")
    public Stock925 query(@PathVariable("id")String id,@RequestParam("date")String date){



        return service.checkStock925(id,date);


    }


}
