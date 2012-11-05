package com.facebook.presto.benchmark;

import com.facebook.presto.TupleInfo.Type;
import com.facebook.presto.nblock.BlockIterable;
import com.facebook.presto.noperator.AggregationOperator;
import com.facebook.presto.noperator.AlignmentOperator;
import com.facebook.presto.noperator.Operator;
import com.facebook.presto.noperator.aggregation.CountAggregation;
import com.facebook.presto.serde.FileBlocksSerde.FileEncoding;
import com.facebook.presto.tpch.TpchSchema.Orders;
import com.facebook.presto.tpch.TpchBlocksProvider;
import com.google.common.collect.ImmutableList;

import static com.facebook.presto.noperator.ProjectionFunctions.singleColumn;

public class CountAggregationOperatorBenchmark
        extends AbstractOperatorBenchmark
{
    public CountAggregationOperatorBenchmark()
    {
        super("op_count_agg", 10, 100);
    }

    @Override
    protected Operator createBenchmarkedOperator(TpchBlocksProvider inputStreamProvider)
    {
        BlockIterable orderKey = inputStreamProvider.getBlocks(Orders.ORDERKEY, FileEncoding.RAW);
        AlignmentOperator alignmentOperator = new AlignmentOperator(orderKey);
        return new AggregationOperator(alignmentOperator, ImmutableList.of(CountAggregation.PROVIDER), ImmutableList.of(singleColumn(Type.FIXED_INT_64, 0, 0)));
    }

    public static void main(String[] args)
    {
        new CountAggregationOperatorBenchmark().runBenchmark(
                new SimpleLineBenchmarkResultWriter(System.out)
        );
    }
}