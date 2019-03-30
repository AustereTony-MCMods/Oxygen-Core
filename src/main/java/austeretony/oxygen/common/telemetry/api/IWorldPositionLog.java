package austeretony.oxygen.common.telemetry.api;

public interface IWorldPositionLog extends ILog {

    int getDimensionId();
    
    int getX();
    
    int getY();
    
    int getZ();
}
