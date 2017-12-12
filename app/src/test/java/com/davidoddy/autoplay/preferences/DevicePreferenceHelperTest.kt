package com.davidoddy.autoplay.preferences

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.preference.ListPreference
import junit.framework.Assert
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

/**
 * Created by doddy on 12/12/17.
 */
class DevicePreferenceHelperTest {

    val device1Address = "00:00:00:00:00"
    val device2Address = "00:00:00:00:01"
    val device3Address = "00:00:00:00:02"

    val device1Name = "Device1"
    val device2Name = "Device2"
    val device3Name = "Device3"

    val device1hasAudio = true
    val device2hasAudio = false
    val device3hasAudio = true


    @Test
    fun loadPreferenceList_Loads_List_Correctly_And_Sets_Default_And_Selects_First_Item_When_List_Is_Not_Empty() {

        val preference = Mockito.mock(ListPreference::class.java)
        Mockito.`when`(preference.entry).thenReturn(device1Name)

        val bluetoothAdapter = createBluetoothAdapter(true)

        DevicePreferenceHelper(bluetoothAdapter).loadPreferenceList(preference)

        val entriesCaptor = ArgumentCaptor.forClass(Array<CharSequence>::class.java)
        val entryValuesCaptor = ArgumentCaptor.forClass(Array<CharSequence>::class.java)
        val defaultValueCaptor = ArgumentCaptor.forClass(String::class.java)
        val summaryCaptor = ArgumentCaptor.forClass(String::class.java)

        Mockito.verify(preference).setEntries(entriesCaptor.capture())
        Mockito.verify(preference).setEntryValues(entryValuesCaptor.capture())
        Mockito.verify(preference).setDefaultValue(defaultValueCaptor.capture())
        Mockito.verify(preference).setSummary(summaryCaptor.capture())

        Assert.assertEquals("Wrong array length for entries.", 2, entriesCaptor.value.size)
        Assert.assertEquals("Wrong entry #1.", device1Name, entriesCaptor.value[0])
        Assert.assertEquals("Wrong entry #2.", device3Name, entriesCaptor.value[1])


        Assert.assertEquals("Wrong array length for values.", 2, entryValuesCaptor.value.size)
        Assert.assertEquals("Wrong value #1.", "$device1Address|$device1Name", entryValuesCaptor.value[0])
        Assert.assertEquals("Wrong value #2.", "$device3Address|$device3Name", entryValuesCaptor.value[1])

        Assert.assertEquals("Wrong default value.", "$device1Address|$device1Name", defaultValueCaptor.value)

        Assert.assertEquals("Wrong summary.", device1Name, summaryCaptor.value)
    }


    @Test
    fun loadPreferenceList_Loads_Empty_List_And_Does_Not_Select_Item_When_List_Is_Empty() {

        val preference = Mockito.mock(ListPreference::class.java)
        Mockito.`when`(preference.entry).thenReturn("")

        val bluetoothAdapter = createBluetoothAdapter(false)

        DevicePreferenceHelper(bluetoothAdapter).loadPreferenceList(preference)

        val entriesCaptor = ArgumentCaptor.forClass(Array<CharSequence>::class.java)
        val entryValuesCaptor = ArgumentCaptor.forClass(Array<CharSequence>::class.java)
        val summaryCaptor = ArgumentCaptor.forClass(String::class.java)

        Mockito.verify(preference).setEntries(entriesCaptor.capture())
        Mockito.verify(preference).setEntryValues(entryValuesCaptor.capture())
        Mockito.verify(preference, Mockito.never()).setDefaultValue(Mockito.any())
        Mockito.verify(preference).setSummary(summaryCaptor.capture())

        Assert.assertEquals("Wrong array length for entries.", 0, entriesCaptor.value.size)
        Assert.assertEquals("Wrong array length for values.", 0, entryValuesCaptor.value.size)
        Assert.assertEquals("Wrong summary.", "", summaryCaptor.value)
    }


    private fun createBluetoothAdapter(fillDevices: Boolean) : BluetoothAdapter {
        val adapter = Mockito.mock(BluetoothAdapter::class.java)
        val deviceSet = when (fillDevices) {
            true -> createDevices()
            else -> emptySet()
        }
        Mockito.`when`(adapter.bondedDevices).thenReturn(deviceSet)
        return adapter
    }


    private fun createDevices() =
            setOf(createDevice(device1Name, device1Address, device1hasAudio),
                    createDevice(device2Name, device2Address, device2hasAudio),
                    createDevice(device3Name, device3Address, device3hasAudio))

    private fun createDevice(name: String, address: String, hasAudio: Boolean) : BluetoothDevice {
        val bluetoothClass = Mockito.mock(BluetoothClass::class.java)
        Mockito.`when`(bluetoothClass.hasService(BluetoothClass.Service.AUDIO)).thenReturn(hasAudio)


        val device = Mockito.mock(BluetoothDevice::class.java)
        Mockito.`when`(device.name).thenReturn(name)
        Mockito.`when`(device.address).thenReturn(address)
        Mockito.`when`(device.bluetoothClass).thenReturn(bluetoothClass)

        return device
    }
}