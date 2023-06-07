package me.emiel.lockdup.model;


import java.util.List;
import java.util.UUID;

public class Cell {
    public String getCellid() {
        return _cellid;
    }

    public String getCellRegionID() {
        return _cellRegionID;
    }

    public void setCellRegionID(String _cellRegion) {
        this._cellRegionID = _cellRegion;
    }

    public String getCellName() {
        return _cellName;
    }

    public void setCellName(String _cellName) {
        this._cellName = _cellName;
    }

    public UUID getOwner() {
        return _owner;
    }

    public void setOwner(UUID _owner) {
        this._owner = _owner;
    }

    public List<UUID> getTrusted() {
        return _trusted;
    }

    public void setTrusted(List<UUID> _trusted) {
        this._trusted = _trusted;
    }

    private final String _cellid;
    private String _cellRegionID;
    private String _cellName;
    private UUID _owner;
    private List<UUID> _trusted;
    private String _world;

    public int get_timeLeft() {
        return _timeLeft;
    }

    public void set_timeLeft(int _timeLeft) {
        this._timeLeft = _timeLeft;
    }

    //in seconds
    private int _timeLeft;
    private int _coins;

    private Size _size;

    public Cell(String cellRegionID, String world, Size size, String cellName, UUID owner, List<UUID> trusted, int timeLeft, int coins) {
        _cellid = UUID.randomUUID().toString();
        _cellRegionID = cellRegionID;
        _cellName = cellName;
        _owner = owner;
        _trusted = trusted;
        _timeLeft = timeLeft;
        _coins = coins;
        _size = size;
        _world = world;
    }

    public int get_coins() {
        return _coins;
    }

    public void set_coins(int _coins) {
        this._coins = _coins;
    }

    public Size get_size() {
        return _size;
    }

    public void set_size(Size _size) {
        this._size = _size;
    }

    public String get_world() {
        return _world;
    }
}
