package exordian_avenger.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import basemod.abstracts.CustomCard;
import exordian_avenger.patches.AbstractCardEnum;
import exordian_avenger.powers.SilencePower;

public class Silence extends CustomCard{
	public static final String ID = "exordian_avenger:silencecard";
	private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "tea/img/cards/silence.png";
	private static final int COST = 0;
	
	public Silence()
	{
		super(ID, NAME,  IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCardEnum.EX_DARK_RED, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ALL_ENEMY);
		this.baseMagicNumber = 1;
		this.magicNumber = this.baseMagicNumber;
	}
	
	@Override
	public void upgrade() {
	    upgradeName();
		upgradeMagicNumber(+1);	
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		   for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
		        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new SilencePower(mo, this.magicNumber), this.magicNumber, true));
		      }
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SilencePower(p, 1), 1));
	}
}