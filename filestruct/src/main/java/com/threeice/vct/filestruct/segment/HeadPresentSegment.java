package com.threeice.vct.filestruct.segment;

import com.threeice.vct.core.ISystemLog;
import com.threeice.vct.core.enums.VctPartEnum;

/**
 * @Author Jason
 * @Description 土地利用现状VCT头
 * @Date  2022/1/12
 * @Param
 * @return
 **/
public class HeadPresentSegment extends VctSegmentBase<HeadPresentSegment>{
    public HeadPresentSegment(ISystemLog systemLog) {
        super(systemLog);
    }

    private String DataMark;
    private String Version;
    private String CoordinateSystemType;
    private int Dim;
    private String XAxisDirection;
    private String YAxisDirection;
    private String XYUnit;
    private String Spheroid;
    private String PrimeMeridian;
    private String Projection;
    private String Parameters;
    private String VerticalDatum;
    private String TemporalReferenceSystem;
    private double xmin;
    private double ymin;
    private double xmax;
    private double ymax;
    private String MapScale;
    private int Offset;
    private String Date;
    private String Separator;

    @Override
    public VctPartEnum getVctPartEnum() {
        return VctPartEnum.HEAD;
    }

    @Override
    public boolean initSegment() {
        return true;
    }

    public String getDataMark() {
        return DataMark;
    }

    public void setDataMark(String dataMark) {
        DataMark = dataMark;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getCoordinateSystemType() {
        return CoordinateSystemType;
    }

    public void setCoordinateSystemType(String coordinateSystemType) {
        CoordinateSystemType = coordinateSystemType;
    }

    public int getDim() {
        return Dim;
    }

    public void setDim(int dim) {
        Dim = dim;
    }

    public String getXAxisDirection() {
        return XAxisDirection;
    }

    public void setXAxisDirection(String XAxisDirection) {
        this.XAxisDirection = XAxisDirection;
    }

    public String getYAxisDirection() {
        return YAxisDirection;
    }

    public void setYAxisDirection(String YAxisDirection) {
        this.YAxisDirection = YAxisDirection;
    }

    public String getXYUnit() {
        return XYUnit;
    }

    public void setXYUnit(String XYUnit) {
        this.XYUnit = XYUnit;
    }

    public String getSpheroid() {
        return Spheroid;
    }

    public void setSpheroid(String spheroid) {
        Spheroid = spheroid;
    }

    public String getPrimeMeridian() {
        return PrimeMeridian;
    }

    public void setPrimeMeridian(String primeMeridian) {
        PrimeMeridian = primeMeridian;
    }

    public String getProjection() {
        return Projection;
    }

    public void setProjection(String projection) {
        Projection = projection;
    }

    public String getParameters() {
        return Parameters;
    }

    public void setParameters(String parameters) {
        Parameters = parameters;
    }

    public String getVerticalDatum() {
        return VerticalDatum;
    }

    public void setVerticalDatum(String verticalDatum) {
        VerticalDatum = verticalDatum;
    }

    public String getTemporalReferenceSystem() {
        return TemporalReferenceSystem;
    }

    public void setTemporalReferenceSystem(String temporalReferenceSystem) {
        TemporalReferenceSystem = temporalReferenceSystem;
    }

    public double getXmin() {
        return xmin;
    }

    public void setXmin(double xmin) {
        this.xmin = xmin;
    }

    public double getYmin() {
        return ymin;
    }

    public void setYmin(double ymin) {
        this.ymin = ymin;
    }

    public double getXmax() {
        return xmax;
    }

    public void setXmax(double xmax) {
        this.xmax = xmax;
    }

    public double getYmax() {
        return ymax;
    }

    public void setYmax(double ymax) {
        this.ymax = ymax;
    }

    public String getMapScale() {
        return MapScale;
    }

    public void setMapScale(String mapScale) {
        MapScale = mapScale;
    }

    public int getOffset() {
        return Offset;
    }

    public void setOffset(int offset) {
        Offset = offset;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getSeparator() {
        return Separator;
    }

    public void setSeparator(String separator) {
        Separator = separator;
    }
}
