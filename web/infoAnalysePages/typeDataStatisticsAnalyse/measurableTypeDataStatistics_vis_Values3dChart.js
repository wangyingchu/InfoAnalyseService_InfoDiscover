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
    var contrastProperty=getQueryString("c");
    if(!xAxisProperty){return;}
    if(!yAxisProperty){return;}
    if(!zAxisProperty){return;}

    var chartType=getQueryString("type");
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

        document.getElementById('graphTitle').innerHTML=discoverSpaceName+" "+measurableType+" "+measurableName;
        document.getElementById('dataSizeText').innerHTML=chartDataSet.length;

        var data = null;
        var graph = null;
        // Create and populate a data table.
        data = new vis.DataSet();

        for(var i=0;i<chartDataSet.length;i++){
            var currentDataDetail=chartDataSet[i];
            var currentData={
                id:i,
                x:getMeasurablePropertyNumberValue(currentDataDetail,xAxisProperty),
                y:getMeasurablePropertyNumberValue(currentDataDetail,yAxisProperty),
                z:getMeasurablePropertyNumberValue(currentDataDetail,zAxisProperty)              
            };
            if(contrastProperty){
                currentData['style']=getMeasurablePropertyNumberValue(currentDataDetail,contrastProperty);
            }
            data.add(currentData);
        }

        // specify options
        var options = {
            width:  '99%',
            style: 'dot',
            // Option tooltip can be true, false, or a function returning a string with HTML contents
            tooltip: function (point) {
                // parameter point contains properties x, y, z, and data
                // data is the original object passed to the point constructor
                var tooltipMessage=xAxisProperty+' (x): <b>' + point.x + '</b><br>'+yAxisProperty+' (y):<b>' + point.y+ '</b><br>'+zAxisProperty+' (z):<b>'+point.z+'</b>';
                if(point.data.style){
                    tooltipMessage=tooltipMessage+'<br>'+contrastProperty+' (c):<b>' + point.data.style+'</b>'
                }
                return tooltipMessage;
            },
            /*
            xValueLabel: function(value) {
                return value;
            },
            yValueLabel: function(value) {
                return value * 10 + '%';
            },
            zValueLabel: function(value) {
                return value / 1000 + 'K';
            },
            */
            xLabel: xAxisProperty+' (x)',
            yLabel: yAxisProperty+' (y)',
            zLabel: zAxisProperty+' (z)',
            showPerspective: true,
            showGrid: true,
            showShadow: false,
            keepAspectRatio: true,
            verticalRatio: 0.5
        };

        if(chartType){
            if((chartType=='dot')||(chartType=='dot-line')||(chartType=='bar')||(chartType=='line')){
                options['style']=chartType;
            }
            if((chartType=='dot-size')||(chartType=='dot-color')||(chartType=='bar-color')||(chartType=='bar-size')){
                if(contrastProperty){
                    options['style']=chartType;
                }
            }
        }

        if(graphHeight){
            var graphHeightValue=Number(graphHeight)-40;
            options['height']=""+graphHeightValue+"px";
        }

        // Instantiate graph object.
        var container = document.getElementById('container');
        graph = new vis.Graph3d(container, data, options);
    });
});
