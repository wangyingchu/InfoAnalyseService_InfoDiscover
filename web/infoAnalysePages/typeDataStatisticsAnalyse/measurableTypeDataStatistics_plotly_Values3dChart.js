$(document).ready(function() {
    var discoverSpaceName=getQueryString("discoverSpace");
    var measurableName=getQueryString("measurableName");
    var graphHeight=getQueryString("graphHeight");
    var graphWidth=getQueryString("graphWidth");
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
    var layoutViewHeight=$(document).height()-60;
    var layoutViewWidth=$(window).width()-20;

    if(graphHeight){
        layoutViewHeight=Number(graphHeight)-40;
    }
    if(graphWidth){
        layoutViewWidth=Number(graphWidth);
    }
    var restURL=restBaseURL+discoverSpaceName+"/"+measurableName+"/"+xAxisProperty+","+yAxisProperty+","+zAxisProperty+"/";

    var plotlyChartRenderFunction=function(err, rows){
        document.getElementById('graphTitle').innerHTML=discoverSpaceName+" "+measurableType+" "+measurableName;
        document.getElementById('dataSizeText').innerHTML=rows.length;
        function unpack(rows, key) {
            return rows.map(function(row) { return row[key]; });
        }
        var data = [{
            name:measurableName,
            x: unpack(rows, xAxisProperty),
            y: unpack(rows, yAxisProperty),
            z: unpack(rows, zAxisProperty),
            mode: 'markers',
            type: 'scatter3d',
            marker: {
                //color: 'rgb(23, 190, 207)',
                color: getCurrentGlobalColor(0),
                size: 4,
                symbol: 'circle',
                line: {
                    color: 'rgb(204, 204, 204)',
                    width: 1
                }
            }
        },{
            name:measurableName,
            alphahull: 7,
            opacity: 0.05,
            type: 'mesh3d',
            color: getCurrentGlobalColor(1),
            x: unpack(rows, xAxisProperty),
            y: unpack(rows, yAxisProperty),
            z: unpack(rows, zAxisProperty)
        }];

        var layout = {
            autosize: true,
            margin: {t: 20,l:20,r:10},
            scene: {
                aspectratio: {
                    x: 2,
                    y: 1,
                    z: 1
                },
                camera: {
                    center: {
                        x: 0,
                        y: 0,
                        z: 0
                    },
                    eye: {
                        x: 1.25,
                        y: 1.25,
                        z: 1.25
                    },
                    up: {
                        x: 0,
                        y: 0,
                        z: 1
                    }
                },
                xaxis: {
                    type: 'linear',
                    zeroline: true,
                    title:xAxisProperty+' (x)'
                },
                yaxis: {
                    type: 'linear',
                    zeroline: true,
                    title:yAxisProperty+' (y)'
                },
                zaxis: {
                    type: 'linear',
                    zeroline: true,
                    title:zAxisProperty+' (z)'
                }
            },
            height: layoutViewHeight,
            width: layoutViewWidth
        };
        Plotly.newPlot('myDiv', data, layout);
    };
    if(querySQL){
        Plotly.d3.json(restURL)
            .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
            .post("querySQL="+querySQL, plotlyChartRenderFunction);
    }else{
        Plotly.d3.json(restURL, plotlyChartRenderFunction);
    }
});

