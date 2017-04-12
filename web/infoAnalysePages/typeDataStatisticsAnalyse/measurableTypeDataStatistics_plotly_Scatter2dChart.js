$(document).ready(function() {
    var discoverSpaceName=getQueryString("discoverSpace");
    var measurableName=getQueryString("measurableName");
    var graphHeight=getQueryString("graphHeight");
    var measurableType=getQueryString("measurableType");

    if(!discoverSpaceName){return;}
    if(!measurableName){return;}
    if(!measurableType){return;}

    var xAxisProperty=getQueryString("x");
    var yAxisProperty=getQueryString("y");
    var scatterProperty=getQueryString("scatter");
    var scatterPropertyValues=getUTF8UrlParam("scatterValues");
    var sizeProperty=getQueryString("size");
    var descProperty=getQueryString("desc");
    var filterProperty=getQueryString("filter");
    var filterValue=getUTF8UrlParam("filterValue");

    if(!xAxisProperty){return;}
    if(!yAxisProperty){return;}
    if(!scatterProperty){return;}
    if(!scatterPropertyValues){return;}
    if(!sizeProperty){return;}

    var querySQL=getQueryString("querySQL");

    var restBaseURL;
    if(measurableType=="FACT"){
        restBaseURL=APPLICATION_REST_SERVICE_CONTEXT+"/ws/typeDataStatisticsAnalyseService/factTypePropertiesJSON/";
    }
    if(measurableType=="DIMENSION"){
        restBaseURL=APPLICATION_REST_SERVICE_CONTEXT+"/ws/typeDataStatisticsAnalyseService/dimensionTypePropertiesJSON/";
    }
    if(measurableType=="RELATION"){
        restBaseURL=APPLICATION_REST_SERVICE_CONTEXT+"/ws/typeDataStatisticsAnalyseService/relationTypePropertiesJSON/";
    }
    if(!restBaseURL){
        return;
    }
    if(graphHeight){
        document.getElementById('my-graph').style.height=""+(Number(graphHeight)-40)+"px";
    }
    var csvProperties=xAxisProperty;
    if(yAxisProperty!=xAxisProperty){
        csvProperties=xAxisProperty+","+yAxisProperty
    }
    if(scatterProperty!=xAxisProperty&&scatterProperty!=yAxisProperty){
        csvProperties=csvProperties+","+scatterProperty;
    }
    if(sizeProperty!=xAxisProperty&&sizeProperty!=yAxisProperty&&sizeProperty!=scatterProperty){
        csvProperties=csvProperties+","+sizeProperty;
    }
    if(descProperty){
        if(descProperty!=xAxisProperty&&descProperty!=yAxisProperty&&descProperty!=scatterProperty&&descProperty!=sizeProperty){
            csvProperties=csvProperties+","+descProperty;
        }
    }
    if(filterProperty){
        if(filterProperty!=xAxisProperty&&filterProperty!=yAxisProperty&&filterProperty!=scatterProperty&&filterProperty!=sizeProperty&&filterProperty!=descProperty){
            csvProperties=csvProperties+","+filterProperty;
        }
    }
    var restURL=restBaseURL+discoverSpaceName+"/"+measurableName+"/"+csvProperties+"/";

    var scatterPropertiesValueArray=scatterPropertyValues.split(",");

    var plotlyChartRenderFunction=function(err, rows) {
        document.getElementById('graphTitle').innerHTML = discoverSpaceName + " " + measurableType + " " + measurableName;
        document.getElementById('dataSizeText').innerHTML = rows.length;
        function unpack(rows, key) {
            return rows.map(function(row) { return row[key]; });
        }
        var data = scatterPropertiesValueArray.map(function(scatterValue) {
            var rowsFiltered = rows.filter(function(row) {
                if(filterProperty&&filterValue){
                    return (row[scatterProperty] === scatterValue) && (+row[filterProperty] == filterValue);
                }else{
                    return (row[scatterProperty] === scatterValue);
                }
            });
            var dataConfig={
                mode: 'markers',
                name: scatterValue,
                x: unpack(rowsFiltered, xAxisProperty),
                y: unpack(rowsFiltered, yAxisProperty),
                marker: {
                    sizemode: 'area',
                    color:globalColorsLoopArray[scatterPropertiesValueArray.indexOf(scatterValue)],
                    size: unpack(rowsFiltered, sizeProperty)
                }
            };
            if(descProperty){
                dataConfig["text"] = unpack(rowsFiltered, descProperty);
            }else{
                dataConfig["text"] = unpack(rowsFiltered, sizeProperty);
            }
            return dataConfig;
        });
        var layout = {
            xaxis: {title: xAxisProperty+' (x)'},
            yaxis: {title: yAxisProperty+' (y)'},
            margin: {t: 20},
            hovermode: 'closest'
        };
        Plotly.plot('my-graph', data, layout, {showLink: false});
    };
    if(querySQL){
        Plotly.d3.json(restURL)
            .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
            .post("querySQL="+querySQL, plotlyChartRenderFunction);
    }else{
        Plotly.d3.json(restURL, plotlyChartRenderFunction);
    }
});

