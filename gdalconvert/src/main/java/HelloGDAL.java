import org.gdal.gdal.Band;

import org.gdal.gdal.Dataset;

import org.gdal.gdal.Driver;

import org.gdal.gdal.gdal;

import org.gdal.gdalconst.gdalconstConstants;

public class HelloGDAL {

    public HelloGDAL(){

        // TODOAuto-generated constructor stub

    }

    public static void main(String[] args) {

        // TODOAuto-generated method stub
        System.out.println(System.getProperty("java.library.path"));
        gdal.AllRegister();

        System.out.println(gdal.GetDriverCount());

        String fileName= "E:\\example.tif";

        // 读取影像数据

        Dataset dataset= gdal.Open(fileName, gdalconstConstants.GA_ReadOnly);

        if (dataset == null){

            System.err.println("GDALOpenfailed - " + gdal.GetLastErrorNo());

            System.err.println(gdal.GetLastErrorMsg());

            System.exit(1);

        }

        Driver driver= dataset.GetDriver();

        System.out.println("Driver:" + driver.getShortName() + "/" + driver.getLongName());

        // 读取影像信息

        int xSize= dataset.getRasterXSize();

        int ySzie= dataset.getRasterYSize();

        int nBandCount= dataset.getRasterCount();

        System.out.println("Sizeis " + xSize + ", " + ySzie + ", " + nBandCount);

        Band band= dataset.GetRasterBand(1);

        int type= band.GetRasterDataType();

        // type为1，代表的是Eightbit unsigned integer

        System.out.println(type);

        dataset.delete();

        gdal.GDALDestroyDriverManager();

    }

}