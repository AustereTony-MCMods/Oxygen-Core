package austeretony.oxygen_core.common.instant;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;

public class InstantDataImpl<T> implements InstantData<T> {

    protected final int index;

    protected final Collector<T> collector;

    protected final Writer<T> writer;

    protected final Reader<T> reader;

    protected final Validator validator;

    protected T value;

    public InstantDataImpl(int index, Collector<T> collector, Writer<T> writer, Reader<T> reader, @Nullable Validator validator) {
        this.index = index;
        this.collector = collector;
        this.writer = writer;
        this.reader = reader;
        this.validator = validator;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public boolean isValid() {
        return this.validator == null || this.validator.validate();
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public void write(EntityLivingBase entityLiving, ByteBuf buffer) {
        this.writer.write(this.collector.collect(entityLiving), buffer);
    }

    @Override
    public void read(ByteBuf buffer) {
        this.value = this.reader.read(buffer);
    }

    @Override
    public InstantData<T> copy() {
        return new InstantDataImpl(this.index, this.collector, this.writer, this.reader, this.validator);
    }

    public static interface Collector<V> {

        V collect(EntityLivingBase entityLiving);
    }

    public static interface Writer<V> {

        void write(V value, ByteBuf buffer);
    }

    public static interface Reader<V> {

        V read(ByteBuf buffer);
    }

    public static interface Validator {

        boolean validate();
    }
}
