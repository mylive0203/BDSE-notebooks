# R 資料輸入

* 常用方法:
```R=
read.table()
read.fwf()
read.xlsx()
read.delim()
read.csv()
scan()
file.choose()
```
### read.table
基本款

> data <-read.table("MRT_201512.csv",sep=",",header=T,encoding="ANSI")

header : T或F取決於是否要顯示欄位名稱
sep : source用什麼去分格資料
encoding : 編碼方式

### read.fwf
固定欄位寬度的read

> data <- read.fwf(file="testOfPerf-0.csv", widths=c(10,10,10))

若呈現太亂，也可以改用 read.fwf 函數，設定好每個欄位的寬度( 利用 widths )來編輯呈現，
在某些由機器產生資料(log)或者(raw dataset)需要大量壓縮的情況特別實用：


###  r-read.fwf
利用 xlsx 套件與 read.xlsx 讀取試算表

> data<-read.xlsx(file="testOfPerf-0.xlsx", sheetIndex=2)

「 read.table 」通常用來讀取 csv、txt 檔案，如果想自由讀取歷年各版本的 excel 檔案，可用「 library 」函數下載「 xlsx 」套件，R會自動安裝運行「 xlsx 」函數所需的其他套件，如「 rJava 」等等。
> library(xlsx)
> Loading required package: xlsxjars
> Loading required package: rJava


安裝完成之後，就可以使用「 read.xlsx 」來讀取檔案了，以名稱為「 testOfPerf-0 」的檔案為例，讀取其中的第 2 張試算表。

sheetIndex=數字用來指定第幾張工作表，也可以使用 sheetName= ”檔案名稱”來指定名稱


r-read.xlsx-範例

呼叫已下載的套件： require

每一次重新開啟 R 後，若需要執行額外下載的套件，可以透過「 require」來呼叫，之後就可以繼續使用相關套件了。
> require(xlsx)
> Loading required package: xlsx
> Loading required package: xlsxjars
> Loading required package: rJava

### scan、read.csv 、 read.delim
依照檔案格式選擇讀取資料的方法：scan、read.csv、read.delim

根據格式不同，以下函數：
* matrix(scan(“檔案名稱”)) # 將向量變數讀取為矩陣形式
* read.csv(“檔案名稱”) # 讀取用逗號分隔的檔案
* read.delim(“檔案名稱”) # 讀取用 tab 分隔的檔案


# 大絕招：file.choose
利用上述函數，結合「 file.choose() 」， R 會彈出對話視窗可供選擇，例如「 read.xlsx() 」加上「 file.choose() 」。
> read.xlsx(file.choose(), sheetIndex=2)


參考網址 : http://molecular-service-science.com/2013/11/02/r-introduction/#more