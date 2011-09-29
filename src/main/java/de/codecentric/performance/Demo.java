package de.codecentric.performance;

/**
 * This class features a few methods which simulate different runtimes. The implementation for each differs slightly,
 * but should not matter. Execution time is guaranteed to be stable around the given times.
 * 
 * Note that this class does not employ best clean code practices :-)
 */
public class Demo {

	/**
	 * invokes the method with given execution time in integer array. Supported methods are: 0, 1, 50, 100, 200, 500
	 * 
	 * @param methods
	 */
	public void runCode(final int... methods) {
		for (int i : methods) {
			switch (i) {
			case 0:
				method0ms();
				break;
			case 1:
				method1ms();
				break;
			case 50:
				method50ms();
				break;
			case 100:
				method100ms();
				break;
			case 200:
				method200ms();
				break;
			case 500:
				method500ms();
				break;
			default:
				System.out.println("Do not now how to run " + i);
				break;
			}
		}
	}

	public final void runFastCode(final long times) {
		for (long i = 0; i < times; i++) {
			method0ms();
		}
	}

	final void method0ms() {
		for (int i = 0; i <= 50; i++) {
			if (i == 50)
				break;
		}
	}

	final void method1ms() {
		long timeMillis = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() > timeMillis + 1)
				return;
		}
	}

	final void method50ms() {
		long timeMillis = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() > timeMillis + 50)
				return;
			try {
				Thread.sleep(51);
			} catch (InterruptedException e) {
				Thread.interrupted();
			}
		}
	}

	final void method100ms() {
		long timeMillis = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() > timeMillis + 100)
				return;
			for (int i = 0; i < 10000; i++) {
				// busy wait
				if (i == 10000)
					break;
			}
		}
	}

	final void method200ms() {
		long timeMillis = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() > timeMillis + 200)
				return;
			for (int i = 0; i < 10000; i++) {
				// busy wait
				if (i == 10000)
					break;
			}
		}
	}

	final void method500ms() {
		long timeMillis = System.currentTimeMillis();
		while (true) {
			if (System.currentTimeMillis() > timeMillis + 500)
				return;
			for (int i = 0; i < 10000; i++) {
				// busy wait
				if (i == 10000)
					break;
			}
		}
	}

}
