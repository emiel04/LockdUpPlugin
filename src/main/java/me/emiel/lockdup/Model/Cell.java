package me.emiel.lockdup.Model;


import java.util.List;
import java.util.UUID;

public class Cell {
    public String get_cellid() {
        return _cellid;
    }

    public String get_cellRegionID() {
        return _cellRegionID;
    }

    public void set_cellRegionID(String _cellRegion) {
        this._cellRegionID = _cellRegion;
    }

    public String get_cellName() {
        return _cellName;
    }

    public void set_cellName(String _cellName) {
        this._cellName = _cellName;
    }

    public UUID get_owner() {
        return _owner;
    }

    public void set_owner(UUID _owner) {
        this._owner = _owner;
    }

    public List<UUID> get_trusted() {
        return _trusted;
    }

    public void set_trusted(List<UUID> _trusted) {
        this._trusted = _trusted;
    }

    private String _cellid;
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
