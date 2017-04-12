package com.infoDiscover.test.demoTestData;

import com.infoDiscover.infoDiscoverEngine.dataMart.Fact;
import com.infoDiscover.infoDiscoverEngine.dataMart.FactType;
import com.infoDiscover.infoDiscoverEngine.dataMart.PropertyType;
import com.infoDiscover.infoDiscoverEngine.infoDiscoverBureau.InfoDiscoverSpace;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineDataMartException;
import com.infoDiscover.infoDiscoverEngine.util.exception.InfoDiscoveryEngineRuntimeException;
import com.infoDiscover.infoDiscoverEngine.util.factory.DiscoverEngineComponentFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by wangychu on 3/7/17.
 */
public class GenetateDemoTestData {

    public static void main(String[] args){
        //gererateIDS();
        //loadScatterFactTypeData();
        //loadBubbleFactTypeData();
        //loadGraph3DFactTypeData();
        //loadTimelineFactTypeData();
    }

    private static void gererateIDS(){
        DiscoverEngineComponentFactory.createInfoDiscoverSpace("chartDS");

        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace("chartDS");
            targetSpace.addFactType("Graph3DFactType");
            FactType _3dGraphFactType=targetSpace.getFactType("Graph3DFactType");
            _3dGraphFactType.addTypeProperty("int01", PropertyType.INT);
            _3dGraphFactType.addTypeProperty("long01", PropertyType.LONG);
            _3dGraphFactType.addTypeProperty("double1",PropertyType.DOUBLE);
            _3dGraphFactType.addTypeProperty("float01",PropertyType.FLOAT);

            targetSpace.addFactType("TimelineFactType");
            FactType _2dGraphFactType=targetSpace.getFactType("TimelineFactType");
            _2dGraphFactType.addTypeProperty("int01", PropertyType.INT);
            _2dGraphFactType.addTypeProperty("long01", PropertyType.LONG);
            _2dGraphFactType.addTypeProperty("double1",PropertyType.DOUBLE);
            _2dGraphFactType.addTypeProperty("float01",PropertyType.FLOAT);
            _2dGraphFactType.addTypeProperty("int02", PropertyType.INT);
            _2dGraphFactType.addTypeProperty("long02", PropertyType.LONG);
            _2dGraphFactType.addTypeProperty("double2",PropertyType.DOUBLE);
            _2dGraphFactType.addTypeProperty("float02",PropertyType.FLOAT);
            _2dGraphFactType.addTypeProperty("int03", PropertyType.INT);
            _2dGraphFactType.addTypeProperty("long03", PropertyType.LONG);
            _2dGraphFactType.addTypeProperty("double3",PropertyType.DOUBLE);
            _2dGraphFactType.addTypeProperty("float03",PropertyType.FLOAT);
            _2dGraphFactType.addTypeProperty("date01",PropertyType.DATE);


            targetSpace.addFactType("ScatterFactType");
            FactType ScatterFactType=targetSpace.getFactType("ScatterFactType");
            ScatterFactType.addTypeProperty("int01", PropertyType.INT);
            ScatterFactType.addTypeProperty("long01", PropertyType.LONG);
            ScatterFactType.addTypeProperty("double1",PropertyType.DOUBLE);
            ScatterFactType.addTypeProperty("float01",PropertyType.FLOAT);
            ScatterFactType.addTypeProperty("string01",PropertyType.STRING);

            targetSpace.addFactType("BubbleFactType");
            FactType BubbleFactType=targetSpace.getFactType("BubbleFactType");
            BubbleFactType.addTypeProperty("int01", PropertyType.INT);
            BubbleFactType.addTypeProperty("long01", PropertyType.LONG);
            BubbleFactType.addTypeProperty("double1",PropertyType.DOUBLE);
            BubbleFactType.addTypeProperty("float01",PropertyType.FLOAT);

        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } catch (InfoDiscoveryEngineDataMartException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    private static void loadScatterFactTypeData(){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace("chartDS");
            String[] stringValueArray=new String[]{"Value A","Value B","Value C","Value D"};
            Random random = new Random();
            for(int i=0;i<1000;i++) {
                Fact targetFact = DiscoverEngineComponentFactory.createFact("ScatterFactType");
                targetFact.setInitProperty("int01", (int) (1000 * Math.random()));
                targetFact.setInitProperty("long01", (long) (500 * Math.random()));
                targetFact.setInitProperty("double01", (double) (100 * Math.random()));
                targetFact.setInitProperty("float01", (float) (100 * Math.random()));
                int idx = random.nextInt(4);
                targetFact.setInitProperty("string01",stringValueArray[idx]);
                targetSpace.addFact(targetFact);
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    private static void loadBubbleFactTypeData(){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace("chartDS");
            for(int i=0;i<500;i++) {
                Fact targetFact = DiscoverEngineComponentFactory.createFact("BubbleFactType");
                targetFact.setInitProperty("int01", (int) (2000 * Math.random()));
                targetFact.setInitProperty("long01", (long) (1500 * Math.random()));
                targetFact.setInitProperty("double01", (double) (200 * Math.random()));
                targetFact.setInitProperty("float01", (float) (100 * Math.random()));
                targetSpace.addFact(targetFact);
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    private static void loadGraph3DFactTypeData(){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace("chartDS");
            for(int i=0;i<250;i++) {
                Fact targetFact = DiscoverEngineComponentFactory.createFact("Graph3DFactType");
                targetFact.setInitProperty("int01", (int) (500 * Math.random()));
                targetFact.setInitProperty("long01", (long) (500 * Math.random()));
                targetFact.setInitProperty("double01", (double) (500 * Math.random()));
                targetFact.setInitProperty("float01", (float) (500 * Math.random()));
                targetSpace.addFact(targetFact);
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }

    public static void loadTimelineFactTypeData(){
        InfoDiscoverSpace targetSpace=null;
        try {
            targetSpace = DiscoverEngineComponentFactory.connectInfoDiscoverSpace("chartDS");
            Calendar baseCalendar=Calendar.getInstance();
            for(int i=0;i<1000;i++) {
                Fact targetFact = DiscoverEngineComponentFactory.createFact("TimelineFactType");
                targetFact.setInitProperty("int01", (int) (1000 * Math.random()));
                targetFact.setInitProperty("long01", (long) (500 * Math.random()));
                targetFact.setInitProperty("double01", (double) (100 * Math.random()));
                targetFact.setInitProperty("float01", (float) (100 * Math.random()));

                targetFact.setInitProperty("int02", (int) (800 * Math.random()));
                targetFact.setInitProperty("long02", (long) (450 * Math.random()));
                targetFact.setInitProperty("double02", (double) (50 * Math.random()));
                targetFact.setInitProperty("float02", (float) (1500 * Math.random()));

                targetFact.setInitProperty("int03", (int) (680 * Math.random()));
                targetFact.setInitProperty("long03", (long) (40 * Math.random()));
                targetFact.setInitProperty("double03", (double) (600 * Math.random()));
                targetFact.setInitProperty("float03", (float) (700 * Math.random()));

                Date currentDate=baseCalendar.getTime();
                baseCalendar.add(Calendar.HOUR,1);
                targetFact.setInitProperty("date01",currentDate);
                targetSpace.addFact(targetFact);
            }
        } catch (InfoDiscoveryEngineRuntimeException e) {
            e.printStackTrace();
        } finally {
            if(targetSpace!=null){
                targetSpace.closeSpace();
            }
        }
    }
}
