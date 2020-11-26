package games.generic.controlModel.heal.old;

import games.generic.controlModel.GModality;
import games.generic.controlModel.heal.AHealableResource;
import games.generic.controlModel.heal.HealingObject;
import games.generic.controlModel.heal.IHealableResourcesHolder;
import games.generic.controlModel.heal.IHealingResourcesOverTimeStrategy;

@Deprecated
public class HealingResourcesOverTimeStrategy_V1FractionsOfTime extends IHealingResourcesOverTimeStrategy {
	private static final long serialVersionUID = 1L;
	public static final int TICKS_PER_TIME_UNIT = 4, LOG_TICKS_PER_TIME_UNIT = 2;

	public HealingResourcesOverTimeStrategy_V1FractionsOfTime(HealingObject objToHeal) {
		super(objToHeal);
		ticksHealing = 0;
		accumulatedTimeLifeRegen = 0;
	}

	protected int ticksHealing;
	protected int accumulatedTimeLifeRegen;

	//

	/**
	 * NON mandatory aspect of this class.<br>
	 * If the "time unit" is subdivided into smaller units which the whole system is
	 * based on and the granularity is too little, then the rigeneration process
	 * could be imperfect (also due to the integer amounts, that makes hard to heal
	 * precisely and efficiently). Then macro-subdivisions of "time-units" and
	 * scaling of those smaller units can both be achieved through tickets.
	 */
	public int getTicksHealing() { return ticksHealing; }

	/**
	 * Returns {@link #TICKS_PER_TIME_UNIT}, see {@link #getTicksHealing()}.
	 */
	public int getTicksPerTimeUnit() { return TICKS_PER_TIME_UNIT; }

	/**
	 * Each "time unit" could be composed by smaller units, as described in
	 * {@link #getTicksHealing()}. For instance, a second is composed by
	 * <code>1000</code> milliseconds and a "turn" is composed by just a single
	 * moment. This method returns that amount. <br>
	 * OverrideProne
	 */
	public int getTimeSubunitsEachTimeUnits() { return 1000; }

	/**
	 * Just calls
	 * <code>{@link #getTimeSubunitsEachTimeUnits()}/{@link #getTicksPerTimeUnit()}</code>.
	 */
	public int getTimeSubunitsEachTicks() { return getTimeSubunitsEachTimeUnits() / getTicksPerTimeUnit(); }

	/**
	 * Protected method, do NOT override.
	 * <p>
	 * Get the accumulated time between each regeneration instance.
	 */
	public int getAccumulatedTimeRegen() { return accumulatedTimeLifeRegen; }

	//

	/** See {@link #getAccumulatedTimeRegen()}. */
	public void setAccumulatedTimeRegen(int newAccumulated) {
		this.accumulatedTimeLifeRegen = newAccumulated;
	}

	/** See {@link #getTicksHealing()}. */
	public void setTicksHealing(int ticks) { this.ticksHealing = ticks; }

	public void performAllHealings(GModality gm) {
		boolean isLastTick;
		int temp, ticksPerTimeUnits;
		IHealableResourcesHolder crh;
		temp = getTicksHealing() + 1;
		ticksPerTimeUnits = getTicksPerTimeUnit();
		isLastTick = (temp >= TICKS_PER_TIME_UNIT);
		if (isLastTick)
			setTicksHealing(0);
		else
			setTicksHealing(temp);
		crh = objToHeal.getHealableResourcesHolder();
		// now apply healing to everybody
		objToHeal.getAllHealableResourceTypes().forEach(ht -> {
			int amountHealed, maxAmount, tempTotalResource;
			AHealableResource cr;
			cr = crh.getHealableResourceFor(ht);
			amountHealed = cr.getRegenerationAmount(); // getHealingRegenerationAmount(ht); // how much does I heal
														// myself?
			if (isLastTick) {
				// calculate the last piece of the cake not currently healed
				amountHealed -= (ticksPerTimeUnits - 1) * //
				(amountHealed / ticksPerTimeUnits);
			} else {
				amountHealed /= ticksPerTimeUnits;
			}
			if (amountHealed > 0) {
				maxAmount = cr.getAmountMax(); // getCurableResourceMax(ht);
				tempTotalResource = cr.getAmount() + amountHealed;
				if (tempTotalResource > maxAmount) {
					// check the maximum cap
					amountHealed -= (tempTotalResource - maxAmount);
					tempTotalResource = maxAmount;
				}
				if (amountHealed > 0) { // there is still an healing instance after the cap-check?
					cr.setAmount(tempTotalResource);
//			crh.getCurableResourceAmount(ht);
					objToHeal.healMyself(objToHeal.newHealInstance(ht, amountHealed));
				}
			}
		});
	}

	@Override
	public void healOverTime(GModality modality, int timeUnits) {
		int tfinal, subunitsEachTicks;
		if (timeUnits <= 0)
			return;
		tfinal = getAccumulatedTimeRegen() + timeUnits;
		subunitsEachTicks = getTimeSubunitsEachTicks();
		if (tfinal > subunitsEachTicks) {
			do {
				tfinal -= subunitsEachTicks;
				setAccumulatedTimeRegen(tfinal);
				performAllHealings(modality);
			} while (tfinal > subunitsEachTicks);
		} else {
			setAccumulatedTimeRegen(tfinal);
		}
	}
}