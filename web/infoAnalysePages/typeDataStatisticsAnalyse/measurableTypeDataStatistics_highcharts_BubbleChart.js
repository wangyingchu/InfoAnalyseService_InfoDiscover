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
    var zAxisProperty=getQueryString("z");

    if(!xAxisProperty){return;}
    if(!yAxisProperty){return;}
    if(!zAxisProperty){return;}

    var xRulerValue=getQueryString("xRuler");
    var yRulerValue=getQueryString("yRuler");

    var querySQL=getQueryString("querySQL");

    var restBaseURL;
    if(measurableType=="FACT"){
        restBaseURL=APPLICATION_REST_SERVICE_CONTEXT+"/ws/typeDataStatisticsAnalyseService/factTypeDataList/";
    }
    if(measurableType=="DIMENSION"){
        restBaseURL=APPLICATION_REST_SERVICE_CONTEXT+"/ws/typeDataStatisticsAnalyseService/dimensionTypeDataList/";
    }
    if(measurableType=="RELATION"){
        restBaseURL=APPLICATION_REST_SERVICE_CONTEXT+"/ws/typeDataStatisticsAnalyseService/relationTypeDataList/";
    }
    if(!restBaseURL){
        return;
    }
    if(graphHeight){
        document.getElementById('container').style.height=""+graphHeight+"px";
    }    
    var restURL=restBaseURL+discoverSpaceName+"/"+measurableName+"/";

    var restConfig={
        url: restURL
    };
    if(querySQL){
        restConfig["type"]="POST";
        restConfig["data"]={
            querySQL:querySQL
        };
    }
    $.ajax(restConfig).then(function(data) {
        var chartDataSet=data.measurableValues;
        var dataSeries= [];
        var zAxisPropertiesNameArray=zAxisProperty.split(",");

        var propertiesDataMap={};
        $.each(zAxisPropertiesNameArray,function(index,value){
            propertiesDataMap[value]=[];
            dataSeries.push({
                data: propertiesDataMap[value],
                type: "bubble",
                name:value,
                color:getCurrentGlobalColor(index)
            });
        });

        var currentDataDetail=null;
        for(var i=0;i<chartDataSet.length;i++){
            currentDataDetail=chartDataSet[i];
            $.each(zAxisPropertiesNameArray,function(index,currentZAxisProperty){
                var currentZValue=getMeasurablePropertyNumberValue(currentDataDetail,currentZAxisProperty);
                propertiesDataMap[currentZAxisProperty].push({
                    x:  getMeasurablePropertyNumberValue(currentDataDetail,xAxisProperty),
                    y:  getMeasurablePropertyNumberValue(currentDataDetail,yAxisProperty),
                    z: currentZValue,
                    value: currentZValue,
                    propertyName: currentZAxisProperty
                })
            });
        }

        var chartConfig= {
            chart: {
                type: 'bubble',
                plotBorderWidth: 1,
                zoomType: 'xy'
            },
            legend: {
                enabled: true
            },
            title: {
                text: discoverSpaceName+" "+measurableType+" "+measurableName
            },
            subtitle: {
                text: "数据总量:" +chartDataSet.length
            },
            xAxis: {
                gridLineWidth: 1,
                title: {
                    text: xAxisProperty+" (x)"
                },
                labels: {
                    format: '{value}'
                }
            },
            yAxis: {
                startOnTick: false,
                endOnTick: false,
                title: {
                    text: yAxisProperty+" (y)"
                },
                labels: {
                    format: '{value}'
                },
                maxPadding: 0.2
            },
            tooltip: {
                useHTML: true,
                headerFormat: '<table>',
                pointFormat:
                '<tr><th>'+xAxisProperty+' (x): </th><td>{point.x}</td></tr>' +
                '<tr><th>'+yAxisProperty+' (y): </th><td>{point.y}</td></tr>' +
                '<tr><th>{point.propertyName} (z): </th><td>{point.z}</td></tr>',
                footerFormat: '</table>',
                followPointer: false
            },
            plotOptions: {
                series: {
                    dataLabels: {
                        enabled: true,
                        format: '{point.value}'
                    }
                }
            },
            series:dataSeries,
            credits: {
                enabled:false
            }
        };

        if(xRulerValue){
            chartConfig["xAxis"]["plotLines"]= [{
                color: 'black',
                dashStyle: 'dot',
                width: 2,
                value: Number(xRulerValue),
                label: {
                    rotation: 0,
                    y: 15,
                    style: {
                        fontStyle: 'italic'
                    },
                    text: xAxisProperty +'='+Number(xRulerValue)
                },
                zIndex: 3
            }];
        }

        if(yRulerValue){
            chartConfig["yAxis"]["plotLines"]= [{
                color: 'black',
                dashStyle: 'dot',
                width: 2,
                value: Number(yRulerValue),
                label: {
                    align: 'right',
                    style: {
                        fontStyle: 'italic'
                    },
                    text: yAxisProperty +'='+Number(yRulerValue),
                    x: -10
                },
                zIndex: 3
            }];
        }

        Highcharts.chart('container', chartConfig);
    });
});
